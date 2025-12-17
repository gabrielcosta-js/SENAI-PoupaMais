package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.adapter.DespesaAdapter;
import com.example.gestaodeprodutos.adapter.MovimentacaoAdapter;
import com.example.gestaodeprodutos.adapter.ReceitaAdapter;
import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.Dados;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.network.SupabaseService;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCarteira extends AppCompatActivity {

    private DadosViewModel dadosViewModel;

    private RecyclerView recyclerEntradas;
    private RecyclerView recyclerSaidas;

    private ReceitaAdapter receitaAdapter;
    private DespesaAdapter despesaAdapter;

    private RecyclerView recyclerView;
    private TextView txtSaldoTotal, txtTotalReceitas, txtTotalDespesas;
    private FloatingActionButton fabAdd;

    private LinearLayout btnHome, btnWallet;
    private ImageView imgHome, imgWallet;
    private TextView txtHome, txtWallet;


    private TextView txt_total_receitas;
    private TextView txt_total_despesas;

    private double totalDespesas = 0.0;
    private double totalReceitas = 0.0;

    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW";

    private final Calendar mesAtual = Calendar.getInstance();

    private List<ReceitaModel> todasReceitas = new ArrayList<>();
    private List<DespesaModel> todasDespesas = new ArrayList<>();

    private List<ReceitaModel> receitasDoMes = new ArrayList<>();
    private List<DespesaModel> despesasDoMes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carteira);

        // 🔥 RECEBER MÊS DA TELA INICIAL
        int mes = getIntent().getIntExtra("MES", -1);
        int ano = getIntent().getIntExtra("ANO", -1);

        if (mes != -1 && ano != -1) {
            mesAtual.set(Calendar.MONTH, mes);
            mesAtual.set(Calendar.YEAR, ano);
        }

        initViews();

        recyclerEntradas = findViewById(R.id.recycler_movimentosEntradas);
        recyclerSaidas = findViewById(R.id.recycler_movimentosSaidas);

        recyclerEntradas.setLayoutManager(new LinearLayoutManager(this));
        recyclerSaidas.setLayoutManager(new LinearLayoutManager(this));

        receitaAdapter = new ReceitaAdapter(new ArrayList<>(), receita -> {
            Intent intent = new Intent(TelaCarteira.this, TelaAlterarReceita.class);
            intent.putExtra("RECEITA", receita);
            startActivity(intent);
        });
        despesaAdapter = new DespesaAdapter(new ArrayList<>(), despesa -> {
            Intent intent = new Intent(TelaCarteira.this, TelaAlterarDespesa.class);
            intent.putExtra("DESPESA", despesa);
            startActivity(intent);
        });



        recyclerEntradas.setAdapter(receitaAdapter);
        recyclerSaidas.setAdapter(despesaAdapter);

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this); // 🔴 ESSENCIAL

        observarDados();

        fabAdd.setOnClickListener(v -> {
            showDialogEscolha();
        });

        btnHome.setOnClickListener(v -> {
            finish();
        });

    }

    private void observarDados() {

        dadosViewModel.getReceita().observe(this, lista -> {
            todasReceitas = lista != null ? lista : new ArrayList<>();
            aplicarFiltroReceita();
        });

        dadosViewModel.getDespesa().observe(this, lista -> {
            todasDespesas = lista != null ? lista : new ArrayList<>();
            aplicarFiltroDespesa();
        });
    }
    private void aplicarFiltroDespesa() {
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

        despesaAdapter.atualizarLista(despesasDoMes);
        txtTotalDespesas.setText("- R$ " + String.format("%.2f", totalDespesas));

        atualizarSaldo();
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

        receitaAdapter.atualizarLista(receitasDoMes);
        txtTotalReceitas.setText("+ R$ " + String.format("%.2f", totalReceitas));

        atualizarSaldo();
    }


    private void atualizarSaldo() {
        double saldo = totalReceitas - totalDespesas;

        txtSaldoTotal.setText(
                "R$ " + String.format("%.2f", saldo)
        );

        // opcional: mudar cor do saldo
        if (saldo < 0) {
            txtSaldoTotal.setTextColor(
                    ContextCompat.getColor(this, R.color.red)
            );
        } else {
            txtSaldoTotal.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
            );
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        dadosViewModel.carregarReceita(token);
        dadosViewModel.carregarDespesa(token);
    }



    private void initViews() {
        recyclerView = findViewById(R.id.recycler_movimentosEntradas);
        txtSaldoTotal = findViewById(R.id.txt_saldo_total);
        txtTotalReceitas = findViewById(R.id.txt_total_receitas);
        txtTotalDespesas = findViewById(R.id.txt_total_despesas);
        fabAdd = findViewById(R.id.fab_add);

        btnHome = findViewById(R.id.btn_home);
        btnWallet = findViewById(R.id.btn_wallet);
        imgHome = findViewById(R.id.img_home);
        imgWallet = findViewById(R.id.img_wallet);
        txtHome = findViewById(R.id.txt_home);
        txtWallet = findViewById(R.id.txt_wallet);

        txtTotalDespesas = findViewById(R.id.txt_total_despesas);
        txtTotalReceitas = findViewById(R.id.txt_total_receitas);
    }

    private void setupNavigation() {
        updateMenuColors(false);

        btnHome.setOnClickListener(v -> {
            updateMenuColors(true);
            startActivity(new Intent(TelaCarteira.this, TelaInicial.class));
            finish();
        });

        btnWallet.setOnClickListener(v -> {
            updateMenuColors(false);
            Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        });
    }

    // --------------------------- SUPABASE ------------------------------





    // --------------------------- INTERFACE ------------------------------



    private void showDialogEscolha() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_escolha, null);

        dialog.setContentView(view);

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout btnReceita = view.findViewById(R.id.btn_escolha_receita);
        LinearLayout btnDespesa = view.findViewById(R.id.btn_escolha_despesa);

        btnReceita.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(TelaCarteira.this, TelaAdicionarReceita.class));
        });

        btnDespesa.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(TelaCarteira.this, TelaAdicionarDespesas.class));
        });

        dialog.show();
    }

    private void updateMenuColors(boolean isHomeActive) {
        int colorActive = ContextCompat.getColor(this, R.color.nav_text_color);
        int colorInactive = ContextCompat.getColor(this, R.color.nav_icon_inactive);

        if (isHomeActive) {
            imgHome.setColorFilter(colorActive);
            txtHome.setTextColor(colorActive);
            imgWallet.setColorFilter(colorInactive);
            txtWallet.setTextColor(colorInactive);
        } else {
            imgHome.setColorFilter(colorInactive);
            txtHome.setTextColor(colorInactive);
            imgWallet.setColorFilter(colorActive);
            txtWallet.setTextColor(colorActive);
        }
    }
}
