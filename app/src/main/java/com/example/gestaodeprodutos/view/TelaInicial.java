package com.example.gestaodeprodutos.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.adapter.DespesaAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TelaInicial extends AppCompatActivity {

    private DadosViewModel dadosViewModel;
    private DespesaAdapter despesaAdapter;
    private TextView txtMesAtual;
    private RecyclerView recyclerViewDespesas;
    private Calendar calendarioAtual;

    private TextView txtSaudacao;

    private TextView txtTotalDespesas;
    private TextView txtTotalReceitas;

    private double totalDespesas = 0.0;
    private double totalReceitas = 0.0;




    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW"; //  Anon key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        calendarioAtual = Calendar.getInstance();


        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        LinearLayout btnHome = findViewById(R.id.btn_home);
        LinearLayout btnWallet = findViewById(R.id.btn_wallet);

        txtMesAtual = findViewById(R.id.txt_mes_atual);
        ImageView btnAvancarMes = findViewById(R.id.btn_avancar_mes);
        ImageView btnVoltarMes = findViewById(R.id.btn_voltar_mes);

        txtTotalDespesas = findViewById(R.id.txt_total_despesas);
        txtTotalReceitas = findViewById(R.id.txt_total_receitas);


        fabAdd.setOnClickListener(v -> {
             showDialogEscolha();
        });

        btnWallet.setOnClickListener(v -> {
            Toast.makeText(this, "Abrir Carteira", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TelaCarteira.class);
            startActivity(intent);
        });

        btnAvancarMes.setOnClickListener(v -> {
            mudarMes(1);
        });

        btnVoltarMes.setOnClickListener(v -> {
            mudarMes(-1);
        });


        recyclerViewDespesas = findViewById(R.id.recycler_despesas_recentes);
        if (recyclerViewDespesas != null) {
            recyclerViewDespesas.setLayoutManager(new LinearLayoutManager(this));
        }

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this);

// Adapter vazio inicialmente
        despesaAdapter = new DespesaAdapter(new ArrayList<>());
        recyclerViewDespesas.setAdapter(despesaAdapter);

// Buscar token salvo
        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");


// Observar resultado das 5 últimas desepsas
        dadosViewModel.getUltimas5Despesas().observe(this, lista -> {

            if (lista == null || lista.isEmpty()) {
                totalDespesas = 0;
                txtTotalDespesas.setText("R$ 0,00");
                despesaAdapter.atualizarLista(new ArrayList<>());
                return;
            }

            despesaAdapter.atualizarLista(lista);

            totalDespesas = 0;
            for (DespesaModel d : lista) {
                totalDespesas += Math.abs(d.getValor());
            }

            atualizarSaldos();
        });


        // Observar resultados das 5 últimas receitas
        dadosViewModel.getUltimas5Receitas().observe(this, lista -> {

            if (lista == null || lista.isEmpty()) {
                totalReceitas = 0;
                txtTotalReceitas.setText("R$ 0,00");
                return;
            }

            totalReceitas = 0;
            for (ReceitaModel r : lista) {
                totalReceitas += r.getValor();
            }

            atualizarSaldos();
        });


        txtSaudacao = findViewById(R.id.txt_saudacao);


        // Mostrar nome do usuário
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        String nome = prefs.getString("nome", "Usuário");

        txtSaudacao.setText("Olá, " + nome);

        despesaAdapter = new DespesaAdapter(new ArrayList<>(), despesa -> {

            Intent intent = new Intent(
                    TelaInicial.this,
                    TelaAlterarDespesa.class
            );

            intent.putExtra("ID", despesa.getId());
            intent.putExtra("VALOR", despesa.getValor());
            intent.putExtra("CATEGORIA", despesa.getCategoria());
            intent.putExtra("DATA", despesa.getData());
            intent.putExtra("DESCRICAO", despesa.getDescricao());
            intent.putExtra("FORMA_PAGAMENTO", despesa.getForma_pagamento());

            startActivity(intent);
        });

        recyclerViewDespesas.setAdapter(despesaAdapter);



    }

    private void atualizarSaldos() {
        txtTotalReceitas.setText(
                "R$ " + String.format(Locale.getDefault(), "%.2f", totalReceitas)
        );

        txtTotalDespesas.setText(
                "R$ " + String.format(Locale.getDefault(), "%.2f", totalDespesas)
        );
    }


    /**
     * Altera o mês exibido e recarrega os dados da lista
     */
    private void mudarMes(int delta) {
        calendarioAtual.add(Calendar.MONTH, delta);

    }

    /**
     * Atualiza o nome do mês e carrega a lista de despesas
     */

    private void showDialogEscolha() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_escolha, null);
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Clique na opção RECEITA
        LinearLayout btnReceita = view.findViewById(R.id.btn_escolha_receita);
        btnReceita.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(TelaInicial.this, TelaAdicionarReceita.class);
            startActivity(intent);
        });

        // Clique na opção DESPESA
        LinearLayout btnDespesa = view.findViewById(R.id.btn_escolha_despesa);
        btnDespesa.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(TelaInicial.this, TelaAdicionarDespesas.class);
            startActivity(intent);
        });

        dialog.show();
    }
    // Carregar as despesas criadas
    @Override
    protected void onResume() {
        super.onResume();

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        dadosViewModel.carregarDespesa(token);
        dadosViewModel.carregarReceita(token);
    }


}