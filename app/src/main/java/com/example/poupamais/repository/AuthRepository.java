package com.example.poupamais.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.poupamais.model.AuthResponse;
import com.example.poupamais.network.ApiAuthService;
import com.example.poupamais.network.RetrofitSupabase;
import com.example.poupamais.model.SignupRequest;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AuthRepository {

    private ApiAuthService api;

    public AuthRepository(Context context) {
        api = RetrofitSupabase
                .getRetrofitInstance(context)
                .create(ApiAuthService.class);
    }


    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW";
    private final String CONTENT = "application/json";



    // LOGIN
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

    // Cadastrar usuário
    public MutableLiveData<AuthResponse> registrarUsuario(String nome, String email, String senha) {
        MutableLiveData<AuthResponse> live = new MutableLiveData<>();

        SignupRequest body = new SignupRequest(nome, email, senha);


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
