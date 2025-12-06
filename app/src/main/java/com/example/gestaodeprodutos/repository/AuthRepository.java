package com.example.gestaodeprodutos.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.gestaodeprodutos.model.AuthResponse;
import com.example.gestaodeprodutos.network.ApiAuthService;
import com.example.gestaodeprodutos.network.RetrofitClient;
import com.example.gestaodeprodutos.view.UsuarioRegistro;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiAuthService api;

    private final String API_KEY = "sb_publishable_eN4SSA3iUkrz-EgXNdJqpQ_Fdh8Dn20";
    private final String CONTENT = "application/json";

    public AuthRepository() {
        api = RetrofitClient.getRetrofitInstance().create(ApiAuthService.class);
    }

    public MutableLiveData<AuthResponse> login(String email, String senha) {

        MutableLiveData<AuthResponse> live = new MutableLiveData<>();

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", senha);

        api.login(API_KEY, CONTENT, body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    live.setValue(response.body());
                } else {
                    live.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                live.setValue(null);
            }
        });

        return live;
    }

    public MutableLiveData<AuthResponse> registrarUsuario(String email, String senha) {
        MutableLiveData<AuthResponse> live = new MutableLiveData<>();

        UsuarioRegistro body = new UsuarioRegistro(email, senha);

        api.registrar(API_KEY, body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                live.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                live.setValue(null);
            }
        });

        return live;
    }
}

