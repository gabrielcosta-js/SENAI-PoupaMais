package com.example.gestaodeprodutos.network;

import com.example.gestaodeprodutos.model.AuthResponse_professor;
import com.example.gestaodeprodutos.view.UsuarioRegistro_professor;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiAuthService_professor {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse_professor> login(
            @Header("apikey") String apiKey,
            @Header("Content-Type") String contentType,
            @Body Map<String, String> body
    );

    @POST("auth/v1/signup")
    Call<AuthResponse_professor> registrar(
            @Header("apikey") String apiKey,
            @Body UsuarioRegistro_professor usuario
    );
}