package com.example.gestaodeprodutos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.AuthResponse_professor;
import com.example.gestaodeprodutos.repository.AuthRepository_professor;

public class AuthViewModel_professor extends ViewModel {

    private AuthRepository_professor repository = new AuthRepository_professor();

    public MutableLiveData<AuthResponse_professor> login(String email, String senha) {
        return repository.login(email, senha);
    }

    public LiveData<AuthResponse_professor> registrar(String email, String senha) {
        return repository.registrarUsuario(email, senha);
    }
}