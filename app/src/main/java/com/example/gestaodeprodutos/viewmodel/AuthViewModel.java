package com.example.gestaodeprodutos.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.AuthResponse;
import com.example.gestaodeprodutos.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private AuthRepository repository;

    // 🔹 OBRIGATÓRIO chamar isso na Activity
    public void init(Context context) {
        if (repository == null) {
            repository = new AuthRepository(context);
        }
    }

    public MutableLiveData<AuthResponse> login(String email, String senha) {
        return repository.login(email, senha);
    }

    public MutableLiveData<AuthResponse> registrar(String nome, String email, String senha) {
        return repository.registrarUsuario(nome, email, senha);
    }
}
