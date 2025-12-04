package com.example.poupamais.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.poupamais.R;
import com.example.poupamais.adapter.ProdutoAdapter;
import com.example.poupamais.model.Produto;
import com.example.poupamais.viewmodel.ProdutoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProdutoViewModel viewModel;
    private ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.recyclerProdutos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //cria o adapter para a lista e um listener para funções dos botões
        adapter = new ProdutoAdapter(new ArrayList<>(), new ProdutoAdapter.ProdutoListener() {
            @Override
            public void onEditarClick(Produto produto) {
                Intent intent = new Intent(MainActivity.this, DetalhesProdutoActivity.class);
                intent.putExtra("produto", produto);
                startActivity(intent);
            }

            @Override
            public void onDeletarClick(Produto produto) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que deseja excluir?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            viewModel.deletarProduto(produto.getId()).observe(MainActivity.this, sucesso -> {
                                if (sucesso) {
                                    Toast.makeText(MainActivity.this, "Produto excluído!", Toast.LENGTH_SHORT).show();
                                    viewModel.carregarProdutos();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(this, DetalhesProdutoActivity.class);

            startActivity(intent);
        });

        // VIEWMODEL
        viewModel = new ViewModelProvider(this).get(ProdutoViewModel.class);

        viewModel.getProdutos().observe(this, produtos -> {
            Log.d("MAIN_DEBUG", "Lista recebida: " + produtos);
            if (produtos != null) adapter.atualizarLista(produtos);
        });

        viewModel.carregarProdutos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.carregarProdutos();
    }
}
