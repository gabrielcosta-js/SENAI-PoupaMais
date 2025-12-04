package com.example.poupamais.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poupamais.R;
import com.example.poupamais.model.Produto;
import com.example.poupamais.viewmodel.ProdutoViewModel;

public class DetalhesProdutoActivity extends AppCompatActivity {

    private EditText edtNome, edtPreco;
    private ProdutoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        edtNome = findViewById(R.id.edtNome);
        edtPreco = findViewById(R.id.txtPreco);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        viewModel = new ViewModelProvider(this).get(ProdutoViewModel.class);
        Produto produto = (Produto) getIntent().getSerializableExtra("produto");
        Boolean isUpdate = produto != null;

        if (isUpdate) {
            edtNome.setText(produto.getNome());
            edtPreco.setText(produto.getPreco().toString());
        }

        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();
            String precoStr = edtPreco.getText().toString();

            if (nome.isEmpty() || precoStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            Double preco = Double.valueOf(precoStr);

            if (isUpdate) {
                viewModel.atualizarProduto(produto.getId(), nome, preco).observe(this, sucesso -> {
                    if (sucesso) {
                        Toast.makeText(this, "Produto salvo!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            viewModel.inserirProduto(nome, preco).observe(this, sucesso -> {
                if (sucesso) {
                    Toast.makeText(this, "Produto salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}