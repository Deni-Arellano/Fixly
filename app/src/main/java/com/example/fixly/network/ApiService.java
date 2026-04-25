package com.example.fixly.network;

import com.example.fixly.models.login.LoginRequest;
import com.example.fixly.models.login.LoginResponse;
import com.example.fixly.models.registroCliente.RegistroRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/")
    Call<ResponseBody> checkStatus();

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("users/registrarCliente")
    Call<RegistroResponse> registrarCliente(@Body RegistroRequest registroRequest);
}
