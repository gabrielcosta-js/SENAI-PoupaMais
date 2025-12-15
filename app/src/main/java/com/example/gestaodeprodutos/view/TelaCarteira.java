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

    private List<Dados> listaUnificada = new ArrayList<>();
    private SupabaseService service;

    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carteira);

        initViews();

        recyclerEntradas = findViewById(R.id.recycler_movimentosEntradas);
        recyclerSaidas = findViewById(R.id.recycler_movimentosSaidas);

        recyclerEntradas.setLayoutManager(new LinearLayoutManager(this));
        recyclerSaidas.setLayoutManager(new LinearLayoutManager(this));

        receitaAdapter = new ReceitaAdapter(new ArrayList<>());
        despesaAdapter = new DespesaAdapter(new ArrayList<>());

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

        dadosViewModel.getReceita().observe(this, receitas -> {
            if (receitas != null) {
                receitaAdapter.atualizarLista(receitas);
            }
        });

        dadosViewModel.getDespesa().observe(this, despesas -> {
            if (despesas != null) {
                despesaAdapter.atualizarLista(despesas);
            }
        });
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
            carregarDadosDoSupabase();
            Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        });
    }

    // --------------------------- SUPABASE ------------------------------

    private void carregarDadosDoSupabase() {
        listaUnificada.clear();

        // Buscar receitas
        service.listarReceita(API_KEY, "Bearer " + API_KEY)
                .enqueue(new Callback<List<ReceitaModel>>() {
                    @Override
                    public void onResponse(Call<List<ReceitaModel>> call, Response<List<ReceitaModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (ReceitaModel r : response.body()) {

                                String desc = (r.getNome_receita() != null && !r.getNome_receita().isEmpty())
                                        ? r.getNome_receita()
                                        : "Receita";

                                String data = (r.getData() != null) ? r.getData() : "--/--";

                                listaUnificada.add(
                                        new Dados(r.getId(), desc, r.getValor(), data, "RECEITA")
                                );
                            }
                        }
                        buscarDespesas();
                    }

                    @Override
                    public void onFailure(Call<List<ReceitaModel>> call, Throwable t) {
                        buscarDespesas();
                    }
                });
    }

    private void buscarDespesas() {

        service.listarDespesa(API_KEY, "Bearer " + API_KEY)
                .enqueue(new Callback<List<DespesaModel>>() {
                    @Override
                    public void onResponse(Call<List<DespesaModel>> call, Response<List<DespesaModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            for (DespesaModel d : response.body()) {

                                String desc = (d.getNome_despesa() != null && !d.getNome_despesa().isEmpty())
                                        ? d.getNome_despesa()
                                        : "Despesa";

                                String data = (d.getData() != null) ? d.getData() : "--/--";

                                listaUnificada.add(
                                        new Dados(d.getId(), desc, d.getValor(), data, "DESPESA")
                                );
                            }
                        }
                        atualizarInterface();
                    }

                    @Override
                    public void onFailure(Call<List<DespesaModel>> call, Throwable t) {
                        atualizarInterface();
                    }
                });
    }

    // --------------------------- INTERFACE ------------------------------

    private void atualizarInterface() {
        double receitaTotal = 0;
        double despesaTotal = 0;

        for (Dados d : listaUnificada) {
            if ("RECEITA".equals(d.getTipo())) {
                receitaTotal += d.getValor();
            } else {
                despesaTotal += d.getValor();
            }
        }

        double saldo = receitaTotal - despesaTotal;

        txtTotalReceitas.setText("+ R$ " + String.format("%.2f", receitaTotal));
        txtTotalDespesas.setText("- R$ " + String.format("%.2f", despesaTotal));
        txtSaldoTotal.setText("R$ " + String.format("%.2f", saldo));

        MovimentacaoAdapter adapter = new MovimentacaoAdapter(listaUnificada);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

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
