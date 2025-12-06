package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;

public class TelaLoginMainActivity extends AppCompatActivity {

    // Declarações das Classes
    private EditText edtLoginEmail;
    private EditText edtLoginSenha;
    private TextView txtLoginEsqueceuSenha;
    private Button btnLoginEntrar;
    private TextView txtLoginCriarConta;

    private DadosViewModel dadosViewModel; // Classe Intermediadora

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login_main);

        // Inicializar ViewModel corretamente
        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);

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
        });

        // Clique: Entrar
        btnLoginEntrar.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString();
            String senha = edtLoginSenha.getText().toString();

            // Validar login
            if (dadosViewModel.validarDadosLogin(email, senha)) {
                Intent intent = new Intent(this, TelaInicial.class);
                startActivity(intent);
                finish(); // fecha a tela de login, liberando memória pro usuário, já que a tela não é mais necessária
            } else {
                edtLoginSenha.setError("Email ou senha incorretos!");
            }
        });

        // Clique: Criar conta
        txtLoginCriarConta.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaCadastroUsuario.class);
            startActivity(intent);
        });
    }
}
