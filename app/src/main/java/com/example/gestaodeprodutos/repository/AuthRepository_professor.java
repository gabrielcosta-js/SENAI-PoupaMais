package com.example.gestaodeprodutos.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.gestaodeprodutos.model.AuthResponse_professor;
import com.example.gestaodeprodutos.network.ApiAuthService_professor;
import com.example.gestaodeprodutos.network.RetrofitClient_professor;
import com.example.gestaodeprodutos.view.UsuarioRegistro_professor;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository_professor {

    private ApiAuthService_professor api;

    private final String API_KEY = "sb_publishable_eN4SSA3iUkrz-EgXNdJqpQ_Fdh8Dn20";
    private final String CONTENT = "application/json";

    public AuthRepository_professor() {
        api = RetrofitClient_professor.getRetrofitInstance().create(ApiAuthService_professor.class);
    }

    public MutableLiveData<AuthResponse_professor> login(String email, String senha) {

        MutableLiveData<AuthResponse_professor> live = new MutableLiveData<>();

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", senha);

        api.login(API_KEY, CONTENT, body).enqueue(new Callback<AuthResponse_professor>() {
            @Override
            public void onResponse(Call<AuthResponse_professor> call, Response<AuthResponse_professor> response) {
                if (response.isSuccessful()) {
                    live.setValue(response.body());
                } else {
                    live.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse_professor> call, Throwable t) {
                live.setValue(null);
            }
        });

        return live;
    }

    public MutableLiveData<AuthResponse_professor> registrarUsuario(String email, String senha) {
        MutableLiveData<AuthResponse_professor> live = new MutableLiveData<>();

        UsuarioRegistro_professor body = new UsuarioRegistro_professor(email, senha);

        api.registrar(API_KEY, body).enqueue(new Callback<AuthResponse_professor>() {
            @Override
            public void onResponse(Call<AuthResponse_professor> call, Response<AuthResponse_professor> response) {
                live.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AuthResponse_professor> call, Throwable t) {
                live.setValue(null);
            }
        });

        return live;
    }
}

