package com.example.gestaodeprodutos.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.AuthViewModel_professor;

public class RegistroUsuarioActivity_professor extends AppCompatActivity {
    private EditText edtEmail, edtSenha;
    private AuthViewModel_professor viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_usuario);

        edtEmail = findViewById(R.id.edtEmailRegistro);
        edtSenha = findViewById(R.id.edtSenhaRegistro);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        viewModel = new ViewModelProvider(this).get(AuthViewModel_professor.class);

        btnRegistrar.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.registrar(email, senha).observe(this, res -> {
                if (res == null) {
                    Toast.makeText(this, "Erro ao registrar", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        });
    }
}