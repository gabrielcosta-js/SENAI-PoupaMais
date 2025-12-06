package com.example.gestaodeprodutos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.AuthResponse;
import com.example.gestaodeprodutos.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private AuthRepository repository = new AuthRepository();

    public MutableLiveData<AuthResponse> login(String email, String senha) {
        return repository.login(email, senha);
    }

    public LiveData<AuthResponse> registrar(String email, String senha) {
        return repository.registrarUsuario(email, senha);
    }
}