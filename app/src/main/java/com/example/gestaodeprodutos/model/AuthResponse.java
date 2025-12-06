package com.example.gestaodeprodutos.model;

public class AuthResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    public User user;

    public String getAccess_token() { return access_token; }
    public String getToken_type() { return token_type; }
    public String getRefresh_token() { return refresh_token; }

    public class User {
        public String id;
        public String email;
    }
}

