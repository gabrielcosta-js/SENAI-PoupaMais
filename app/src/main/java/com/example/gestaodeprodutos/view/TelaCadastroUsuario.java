package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.AuthViewModel;
import com.example.gestaodeprodutos.viewmodel.AuthViewModel_professor;

public class TelaCadastroUsuario extends AppCompatActivity {

    private EditText edtCadastroNome;
    private EditText edtCadastroEmail;
    private EditText edtCadastroSenha;
    private Button btnCadastroCriarConta;
    private TextView txtCadastroIniciarLogin;

    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro_usuario);

        // Configuração dos insets (apenas isso!)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // INICIALIZAÇÕES
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.init(this); // 🔴 LINHA QUE FALTAVA


        edtCadastroNome = findViewById(R.id.edtCadastroNome);
        edtCadastroEmail = findViewById(R.id.edtCadastroEmail);
        edtCadastroSenha = findViewById(R.id.edtCadastroSenha);
        btnCadastroCriarConta = findViewById(R.id.btnCadastroCriarConta);
        txtCadastroIniciarLogin = findViewById(R.id.txtCadastroIniciarLogin);

        // BOTÃO: CRIAR CONTA
        btnCadastroCriarConta.setOnClickListener(v -> {
            String nome = edtCadastroNome.getText().toString().trim();
            String email = edtCadastroEmail.getText().toString().trim();
            String senha = edtCadastroSenha.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.registrar(nome, email, senha).observe(this, res -> {
                if (res == null) {
                    Toast.makeText(this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_LONG).show();
                    finish(); // fecha SÓ depois do sucesso
                }
            });
        });



        // TEXTO: JÁ TENHO LOGIN
        txtCadastroIniciarLogin.setOnClickListener(v -> {
            finish();
        });
    }
}
