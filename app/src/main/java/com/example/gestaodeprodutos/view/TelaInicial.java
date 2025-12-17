package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import com.google.android.material.button.MaterialButton;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TelaInicial extends AppCompatActivity {

    private DadosViewModel dadosViewModel;
    private DespesaAdapter despesaAdapter;

    private TextView txtSaudacao, txtTotalDespesas, txtTotalReceitas, txt_mes_atual;
    private RecyclerView recyclerViewDespesas;

    private PieChart pieChart;
    private BarChart barChart;

    private ImageView btn_voltar_mes, btn_avancar_mes;

    private final Calendar mesAtual = Calendar.getInstance();

    private final Map<String, Integer> coresCategoria = new HashMap<>();
    private CategoriaFiltro categoriaSelecionada = CategoriaFiltro.TODOS;

    private List<DespesaModel> todasDespesas = new ArrayList<>();
    private List<DespesaModel> despesasDoMes = new ArrayList<>();
    private List<ReceitaModel> receitasDoMes = new ArrayList<>();

    private List<ReceitaModel> todasReceitas = new ArrayList<>();


    private double totalDespesas = 0;
    private double totalReceitas = 0;

    private MaterialButton btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        inicializarCores();
        inicializarViews();
        atualizarTextoMes();

        despesaAdapter = new DespesaAdapter(new ArrayList<>(), despesa -> {
            Intent intent = new Intent(this, TelaAlterarDespesa.class);
            intent.putExtra("DESPESA", despesa);
            startActivity(intent);
        });

        configurarRecycler();
        configurarBotoesCategoria();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        LinearLayout btnWallet = findViewById(R.id.btn_wallet);

        fabAdd.setOnClickListener(v -> showDialogEscolha());
        btnWallet.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaCarteira.class);
            intent.putExtra("MES", mesAtual.get(Calendar.MONTH));
            intent.putExtra("ANO", mesAtual.get(Calendar.YEAR));
            startActivity(intent);
        });


        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this);

        observarDados();

        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        txtSaudacao.setText("Olá, " + prefs.getString("nome", "Usuário"));

        btn_voltar_mes.setOnClickListener(v -> {
            mesAtual.add(Calendar.MONTH, -1);
            atualizarTextoMes();
            aplicarFiltroMes();
            aplicarFiltroReceita();
        });

        btn_avancar_mes.setOnClickListener(v -> {
            mesAtual.add(Calendar.MONTH, 1);
            atualizarTextoMes();
            aplicarFiltroMes();
            aplicarFiltroReceita();
        });

        btnSair.setOnClickListener(v -> {

            // 🔹 Limpa token
            SharedPreferences prefsApp = getSharedPreferences("APP", MODE_PRIVATE);
            prefsApp.edit().clear().apply();

            // 🔹 Limpa dados do usuário
            SharedPreferences prefsUser = getSharedPreferences("usuario", MODE_PRIVATE);
            prefsUser.edit().clear().apply();

            // 🔹 Vai para login limpando todas as telas
            Intent intent = new Intent(TelaInicial.this, TelaLoginMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });

    }

    /* ---------------- OBSERVERS ---------------- */

    private void observarDados() {

        dadosViewModel.getDespesa().observe(this, lista -> {
            todasDespesas = lista != null ? lista : new ArrayList<>();
            aplicarFiltroMes(); // 🔥 garante atualização imediata
        });

        dadosViewModel.getReceita().observe(this, lista -> {
            todasReceitas = lista != null ? lista : new ArrayList<>();
            aplicarFiltroReceita(); // 🔥 recalcula corretamente
        });

    }
    private void aplicarFiltroReceita() {
        receitasDoMes.clear();
        totalReceitas = 0;

        int mesSelecionado = mesAtual.get(Calendar.MONTH) + 1;
        int anoSelecionado = mesAtual.get(Calendar.YEAR);

        for (ReceitaModel r : todasReceitas) {
            if (r.getData() == null || r.getData().isEmpty()) continue;

            String[] partes = r.getData().split("-");
            if (partes.length < 3) continue;

            int ano = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);

            if (mes == mesSelecionado && ano == anoSelecionado) {
                receitasDoMes.add(r);
                totalReceitas += r.getValor();
            }
        }

        atualizarSaldos();
    }


    /* ---------------- FILTRO DE MÊS ---------------- */

    private void aplicarFiltroMes() {
        despesasDoMes.clear();
        totalDespesas = 0;

        int mesSelecionado = mesAtual.get(Calendar.MONTH) + 1;
        int anoSelecionado = mesAtual.get(Calendar.YEAR);

        for (DespesaModel d : todasDespesas) {
            if (d.getData() == null || d.getData().isEmpty()) continue;

            String[] partes = d.getData().split("-");
            if (partes.length < 3) continue;

            int ano = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);

            if (mes == mesSelecionado && ano == anoSelecionado) {
                despesasDoMes.add(d);
                totalDespesas += Math.abs(d.getValor());
            }
        }

        despesasDoMes.sort((d1, d2) -> d2.getId() - d1.getId());

        List<DespesaModel> ultimas5 = despesasDoMes.subList(
                0, Math.min(despesasDoMes.size(), 5)
        );

        despesaAdapter.atualizarLista(ultimas5);
        atualizarSaldos();
        selecionarCategoria(categoriaSelecionada);
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

        for (DespesaModel d : despesasDoMes) {
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

        for (DespesaModel d : despesasDoMes) {
            if (categoria.equalsIgnoreCase(d.getCategoria())) {
                entries.add(new BarEntry(index++, (float) Math.abs(d.getValor())));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, categoria);
        dataSet.setColor(coresCategoria.getOrDefault(categoria, Color.GRAY));

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    /* ---------------- UI ---------------- */

    private void atualizarSaldos() {
        txtTotalReceitas.setText("R$ " + String.format("%.2f", totalReceitas));
        txtTotalDespesas.setText("R$ " + String.format("%.2f", totalDespesas));
    }

    private void atualizarTextoMes() {
        int mes = mesAtual.get(Calendar.MONTH);
        int ano = mesAtual.get(Calendar.YEAR);

        String nomeMes = new DateFormatSymbols(new Locale("pt", "BR"))
                .getMonths()[mes];

        nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
        txt_mes_atual.setText(nomeMes + " " + ano);
    }

    private void inicializarViews() {
        txtSaudacao = findViewById(R.id.txt_saudacao);
        txtTotalDespesas = findViewById(R.id.txt_total_despesas);
        txtTotalReceitas = findViewById(R.id.txt_total_receitas);
        txt_mes_atual = findViewById(R.id.txt_mes_atual);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);

        btnSair = findViewById(R.id.btn_sair);
        btn_voltar_mes = findViewById(R.id.btn_voltar_mes);
        btn_avancar_mes = findViewById(R.id.btn_avancar_mes);
    }

    private void configurarRecycler() {
        recyclerViewDespesas = findViewById(R.id.recycler_despesas_recentes);
        recyclerViewDespesas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDespesas.setAdapter(despesaAdapter);
    }

    private void configurarBotoesCategoria() {
        findViewById(R.id.btn_categoria_todos).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.TODOS));
        findViewById(R.id.btn_categoria_mercado).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.MERCADO));
        findViewById(R.id.btn_categoria_alimentacao).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.ALIMENTACAO));
        findViewById(R.id.btn_categoria_transporte).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.TRANSPORTE));
        findViewById(R.id.btn_categoria_moradia).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.MORADIA));
        findViewById(R.id.btn_categoria_saude).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.SAUDE));
        findViewById(R.id.btn_categoria_lazer).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.LAZER));
        findViewById(R.id.btn_categoria_outros).setOnClickListener(v -> selecionarCategoria(CategoriaFiltro.OUTROS));
    }

    private void inicializarCores() {
        coresCategoria.put("Mercado", Color.parseColor("#4CAF50"));
        coresCategoria.put("Alimentação", Color.parseColor("#FF9800"));
        coresCategoria.put("Transporte", Color.parseColor("#2196F3"));
        coresCategoria.put("Moradia", Color.parseColor("#9C27B0"));
        coresCategoria.put("Saúde", Color.parseColor("#F44336"));
        coresCategoria.put("Lazer", Color.parseColor("#00BCD4"));
        coresCategoria.put("Outros", Color.parseColor("#9E9E9E"));
    }

    private void showDialogEscolha() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_escolha, null);
        dialog.setContentView(view);

        view.findViewById(R.id.btn_escolha_receita).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, TelaAdicionarReceita.class));
        });

        view.findViewById(R.id.btn_escolha_despesa).setOnClickListener(v -> {
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
