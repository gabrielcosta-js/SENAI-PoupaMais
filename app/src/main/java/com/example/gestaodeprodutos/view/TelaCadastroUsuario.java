package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;

public class TelaCadastroUsuario extends AppCompatActivity {

    private EditText edtCadastroNome;
    private EditText edtCadastroEmail;
    private EditText edtCadastroSenha;
    private Button btnCadastroCriarConta;
    private TextView txtCadastroIniciarLogin;
    private DadosViewModel dadosViewModel;

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
        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);

        edtCadastroNome = findViewById(R.id.edtCadastroNome);
        edtCadastroEmail = findViewById(R.id.edtCadastroEmail);
        edtCadastroSenha = findViewById(R.id.edtCadastroSenha);
        btnCadastroCriarConta = findViewById(R.id.btnCadastroCriarConta);
        txtCadastroIniciarLogin = findViewById(R.id.txtCadastroIniciarLogin);

        // BOTÃO: CRIAR CONTA
        btnCadastroCriarConta.setOnClickListener(v -> {
            String nome = edtCadastroNome.getText().toString();
            String email = edtCadastroEmail.getText().toString();
            String senha = edtCadastroSenha.getText().toString();

            // Envia os dados para o ViewModel
            dadosViewModel.setCadastrarUsuario(nome, email, senha);

            // Vai para tela de Login
            Intent intent = new Intent(this, TelaLoginMainActivity.class);
            startActivity(intent);

            finish(); // fecha tela de cadastro
        });

        // TEXTO: JÁ TENHO LOGIN
        txtCadastroIniciarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaLoginMainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
