package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("data")
    private UserData data;

    public SignupRequest(String nome, String email, String senha) {
        this.email = email;
        this.password = senha;
        this.data = new UserData(nome);
    }

    static class UserData {
        @SerializedName("nome")
        private String nome;

        public UserData(String nome) {
            this.nome = nome;
        }
    }
}
