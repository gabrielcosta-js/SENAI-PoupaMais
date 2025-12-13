package com.example.gestaodeprodutos.network;

import com.example.gestaodeprodutos.model.AuthResponse_professor;
import com.example.gestaodeprodutos.view.TelaCadastroUsuario;

import java.util.Map;

import com.example.gestaodeprodutos.model.AuthResponse;
import com.example.gestaodeprodutos.model.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiAuthService {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(
            @Header("apikey") String apiKey,
            @Header("Content-Type") String contentType,
            @Body Map<String, String> body
    );

    @POST("auth/v1/signup")
    Call<AuthResponse> registrar(
            @Header("apikey") String apiKey,
            @Body SignupRequest body
    );

}
