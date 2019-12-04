package com.scanbar.taskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.scanbar.taskapp.Model.LoginWrapper;
import com.scanbar.taskapp.Model.Token;
import com.scanbar.taskapp.services.IAuthService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );
    private IAuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://task-app-ana-api.herokuapp.com/") //localhost for emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(IAuthService.class);

    }

    public void logIn(View v) {

        EditText userEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.password);

        final String user = userEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final LoginActivity loginActivityObject = this;

        if(user.isEmpty()) {
            userEditText.setError("User can't be empty");
        } if(password.isEmpty()) {
            passwordEditText.setError("Password can't be empty");
        }

        else {

            executorService.execute( new Runnable() {
                @Override
                public void run() {

                    try {
                        Response<Token> response = authService.login(new LoginWrapper(user, password)).execute();
                        Token token = response.body();
                        SharedPreferences sharedPref =
                                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                        if(token == null) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(loginActivityObject );
                            builder.setTitle("Error");
                            builder.setMessage("Not valid user or password, please try again...");
                            AlertDialog alertDialog = builder.create();

                            alertDialog.show();
                            return;
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("TOKEN_KEY", "Bearer "+ token.getAccessToken());
                        editor.commit();

                        Intent intent = new Intent(loginActivityObject, MainActivity.class);
                        startActivity(intent);
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            } );
        }
    }
}
