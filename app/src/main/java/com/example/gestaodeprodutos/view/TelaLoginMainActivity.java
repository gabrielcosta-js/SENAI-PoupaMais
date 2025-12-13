package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.AuthViewModel;

public class TelaLoginMainActivity extends AppCompatActivity {

    // Declarações das Classes
    private EditText edtLoginEmail;
    private EditText edtLoginSenha;
    private TextView txtLoginEsqueceuSenha;
    private Button btnLoginEntrar;
    private TextView txtLoginCriarConta;

    private AuthViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login_main);

        // Inicializar ViewModel corretamente
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.init(this);


        // Conectar aos IDs
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginSenha = findViewById(R.id.edtLoginSenha);
        txtLoginEsqueceuSenha = findViewById(R.id.txtLoginEsqueceuSenha);
        btnLoginEntrar = findViewById(R.id.btnLoginEntrar);
        txtLoginCriarConta = findViewById(R.id.txtLoginCriarConta);

        // Clique: Esqueceu a senha
        txtLoginEsqueceuSenha.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaEsqueceuSenha.class);
            startActivity(intent);
            finish();
        });

        // Clique: Entrar
        btnLoginEntrar.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString();
            String senha = edtLoginSenha.getText().toString();

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

                startActivity(new Intent(this, TelaInicial.class));
                finish();
            });


        });

        // Clique: Criar conta
        txtLoginCriarConta.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaCadastroUsuario.class);
            startActivity(intent);
            finish();
        });
    }
}
