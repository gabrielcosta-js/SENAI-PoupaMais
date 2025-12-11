package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.AuthViewModel_professor;

public class LoginActivity_professor extends AppCompatActivity {
    private EditText edtEmail, edtSenha;
    private AuthViewModel_professor viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        Button btnLogin = findViewById(R.id.btnLogin);

        viewModel = new ViewModelProvider(this).get(AuthViewModel_professor.class);

        btnLogin.setOnClickListener(v -> {

            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha email e senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, senha).observe(this, res -> {

                if (res == null) {
                    Toast.makeText(this, "Login inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Salvar token
                getSharedPreferences("APP", MODE_PRIVATE)
                        .edit()
                        .putString("TOKEN", res.getToken_type() + " " + res.getAccess_token())
                        .apply();

                startActivity(new Intent(this, MainActivity_professor.class));
                finish();
            });
        });
    }

    public void cadastrarUsuario(View v) {
        Intent intent = new Intent(this, RegistroUsuarioActivity_professor.class);

        startActivity(intent);
    }
}