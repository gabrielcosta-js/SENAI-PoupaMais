package com.example.gestaodeprodutos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.adapter.ProdutoAdapter_professor;
import com.example.gestaodeprodutos.model.Produto_professor;
import com.example.gestaodeprodutos.viewmodel.ProdutoViewModel_professor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity_professor extends AppCompatActivity {

    private ProdutoViewModel_professor viewModel;
    private ProdutoAdapter_professor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.recyclerProdutos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        String token = this.getSharedPreferences("APP", MODE_PRIVATE)
                .getString("token", null);

        //cria o adapter para a lista e um listener para funções dos botões
        adapter = new ProdutoAdapter_professor(new ArrayList<>(), new ProdutoAdapter_professor.ProdutoListener() {
            @Override
            public void onEditarClick(Produto_professor produto) {
                Intent intent = new Intent(MainActivity_professor.this, DetalhesProdutoActivity_professor.class);
                intent.putExtra("produto", produto);
                startActivity(intent);
            }

            @Override
            public void onDeletarClick(Produto_professor produto) {
                new AlertDialog.Builder(MainActivity_professor.this)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que deseja excluir?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            viewModel.deletarProduto(produto.getId(), token).observe(MainActivity_professor.this, sucesso -> {
                                if (sucesso) {
                                    Toast.makeText(MainActivity_professor.this, "Produto excluído!", Toast.LENGTH_SHORT).show();
                                    viewModel.carregarProdutos(token);
                                } else {
                                    Toast.makeText(MainActivity_professor.this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddProduto);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetalhesProdutoActivity_professor.class);

            startActivity(intent);
        });

        // VIEWMODEL
        viewModel = new ViewModelProvider(this).get(ProdutoViewModel_professor.class);

        viewModel.getProdutos().observe(this, produtos -> {
            Log.d("MAIN_DEBUG", "Lista recebida: " + produtos);
            if (produtos != null) adapter.atualizarLista(produtos);
        });

        viewModel.carregarProdutos(token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = this.getSharedPreferences("APP", MODE_PRIVATE)
                .getString("token", null);

        viewModel.carregarProdutos(token);
    }
}
