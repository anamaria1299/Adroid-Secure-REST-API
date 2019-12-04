package com.scanbar.taskapp.services;

import com.scanbar.taskapp.Model.LoginWrapper;
import com.scanbar.taskapp.Model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {

    @POST("users/login/")
    Call<Token> login(@Body LoginWrapper login);
}
