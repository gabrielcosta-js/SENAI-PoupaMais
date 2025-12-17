package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.adapter.DespesaAdapter;
import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.util.CategoriaFiltro;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TelaInicial extends AppCompatActivity {

    private DadosViewModel dadosViewModel;
    private DespesaAdapter despesaAdapter;

    private TextView txtSaudacao, txtTotalDespesas, txtTotalReceitas;
    private RecyclerView recyclerViewDespesas;

    private PieChart pieChart;
    private BarChart barChart;

    private double totalDespesas = 0.0;
    private double totalReceitas = 0.0;

    private List<DespesaModel> listaDespesas = new ArrayList<>();

    private CategoriaFiltro categoriaSelecionada = CategoriaFiltro.TODOS;
    private final Map<String, Integer> coresCategoria = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        inicializarCores();
        inicializarViews();

        despesaAdapter = new DespesaAdapter(new ArrayList<>(), despesa -> {
            Intent intent = new Intent(this, TelaAlterarDespesa.class);
            intent.putExtra("DESPESA", despesa); // 🔥 ISSO É O MAIS IMPORTANTE
            startActivity(intent);
        });


        configurarRecycler();
        configurarBotoesCategoria();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        LinearLayout btnWallet = findViewById(R.id.btn_wallet);

        fabAdd.setOnClickListener(v -> showDialogEscolha());
        btnWallet.setOnClickListener(v ->
                startActivity(new Intent(this, TelaCarteira.class))
        );

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this);

        observarDados();

        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        txtSaudacao.setText("Olá, " + prefs.getString("nome", "Usuário"));
    }

    /* ---------------- INICIALIZAÇÕES ---------------- */

    private void inicializarViews() {
        txtSaudacao = findViewById(R.id.txt_saudacao);
        txtTotalDespesas = findViewById(R.id.txt_total_despesas);
        txtTotalReceitas = findViewById(R.id.txt_total_receitas);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
    }

    private void configurarRecycler() {
        recyclerViewDespesas = findViewById(R.id.recycler_despesas_recentes);
        recyclerViewDespesas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDespesas.setAdapter(despesaAdapter);
    }

    private void configurarBotoesCategoria() {
        findViewById(R.id.btn_categoria_todos)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.TODOS));
        findViewById(R.id.btn_categoria_mercado)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.MERCADO));
        findViewById(R.id.btn_categoria_alimentacao)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.ALIMENTACAO));
        findViewById(R.id.btn_categoria_transporte)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.TRANSPORTE));
        findViewById(R.id.btn_categoria_moradia)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.MORADIA));
        findViewById(R.id.btn_categoria_saude)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.SAUDE));
        findViewById(R.id.btn_categoria_lazer)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.LAZER));
        findViewById(R.id.btn_categoria_outros)
                .setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.OUTROS));
    }

    /* ---------------- OBSERVERS ---------------- */

    private void observarDados() {

        // 🔹 5 últimas despesas (Recycler)
        dadosViewModel.getUltimas5Despesas().observe(this, lista -> {
            despesaAdapter.atualizarLista(
                    lista != null ? lista : new ArrayList<>()
            );
        });

        // 🔹 Todas as despesas (totais + gráficos)
        dadosViewModel.getDespesa().observe(this, lista -> {
            listaDespesas = lista != null ? lista : new ArrayList<>();

            totalDespesas = 0;
            for (DespesaModel d : listaDespesas) {
                totalDespesas += Math.abs(d.getValor());
            }

            atualizarSaldos();
            selecionarCategoria(categoriaSelecionada);
        });

        // 🔹 Receitas (total)
        dadosViewModel.getReceita().observe(this, lista -> {
            totalReceitas = 0;
            if (lista != null) {
                for (ReceitaModel r : lista) {
                    totalReceitas += r.getValor();
                }
            }
            atualizarSaldos();
        });
    }

    private void atualizarSaldos() {
        txtTotalReceitas.setText("R$ " +
                String.format(Locale.getDefault(), "%.2f", totalReceitas));
        txtTotalDespesas.setText("R$ " +
                String.format(Locale.getDefault(), "%.2f", totalDespesas));
    }

    /* ---------------- GRÁFICOS ---------------- */

    private void selecionarCategoria(CategoriaFiltro categoria) {
        categoriaSelecionada = categoria;

        if (categoria == CategoriaFiltro.TODOS) {
            mostrarGraficoTodos();
        } else {
            mostrarGraficoBarra(categoria.name());
        }
    }

    private void mostrarGraficoTodos() {
        pieChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);

        Map<String, Float> somaCategoria = new HashMap<>();

        for (DespesaModel d : listaDespesas) {
            somaCategoria.put(
                    d.getCategoria(),
                    somaCategoria.getOrDefault(d.getCategoria(), 0f)
                            + (float) Math.abs(d.getValor())
            );
        }

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> cores = new ArrayList<>();

        for (String categoria : somaCategoria.keySet()) {
            entries.add(new PieEntry(somaCategoria.get(categoria), categoria));
            cores.add(coresCategoria.getOrDefault(categoria, Color.GRAY));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(cores);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setUsePercentValues(true);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void mostrarGraficoBarra(String categoria) {
        pieChart.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);

        List<BarEntry> entries = new ArrayList<>();
        int index = 0;

        for (DespesaModel d : listaDespesas) {
            if (categoria.equalsIgnoreCase(d.getCategoria())) {
                entries.add(
                        new BarEntry(index++, (float) Math.abs(d.getValor()))
                );
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, categoria);
        dataSet.setColor(coresCategoria.getOrDefault(categoria, Color.GRAY));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    /* ---------------- CORES ---------------- */

    private void inicializarCores() {
        coresCategoria.put("Mercado", Color.parseColor("#4CAF50"));
        coresCategoria.put("Alimentação", Color.parseColor("#FF9800"));
        coresCategoria.put("Transporte", Color.parseColor("#2196F3"));
        coresCategoria.put("Moradia", Color.parseColor("#9C27B0"));
        coresCategoria.put("Saúde", Color.parseColor("#F44336"));
        coresCategoria.put("Lazer", Color.parseColor("#00BCD4"));
        coresCategoria.put("Outros", Color.parseColor("#9E9E9E"));
    }

    /* ---------------- DIALOG ---------------- */

    private void showDialogEscolha() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_escolha, null);
        dialog.setContentView(view);

        view.findViewById(R.id.btn_escolha_receita)
                .setOnClickListener(v -> {
                    dialog.dismiss();
                    startActivity(new Intent(this, TelaAdicionarReceita.class));
                });

        view.findViewById(R.id.btn_escolha_despesa)
                .setOnClickListener(v -> {
                    dialog.dismiss();
                    startActivity(new Intent(this, TelaAdicionarDespesas.class));
                });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        dadosViewModel.carregarDespesa(token);
        dadosViewModel.carregarReceita(token);
    }
}
