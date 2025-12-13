package com.example.gestaodeprodutos.model;

import java.util.HashMap;
import java.util.Map;

public class SignupRequest {
    public String email;
    public String password;
    public Map<String, String> data;

    public SignupRequest(String email, String password, String nome) {
        this.email = email;
        this.password = password;

        data = new HashMap<>();
        data.put("nome", nome);
    }
}

