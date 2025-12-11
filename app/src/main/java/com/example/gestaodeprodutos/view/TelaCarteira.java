package com.example.gestaodeprodutos.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.adapter.DadosAdapter;
import com.example.gestaodeprodutos.model.Dados;
import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.network.RetrofitClient_professor;
import com.example.gestaodeprodutos.network.SupabaseService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCarteira extends AppCompatActivity {

    // Componentes de Dados
    private RecyclerView recyclerView;
    private TextView txtSaldoTotal, txtTotalReceitas, txtTotalDespesas;
    private FloatingActionButton fabAdd;

    // Componentes da Barra de Navegação
    private LinearLayout btnHome, btnWallet;
    private ImageView imgHome, imgWallet;
    private TextView txtHome, txtWallet;

    // Variáveis Lógicas
    private List<Dados> listaUnificada = new ArrayList<>();
    private SupabaseService service;

    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW"; //  Anon key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- CORREÇÃO AQUI: Aponta para o seu layout correto ---
        setContentView(R.layout.activity_tela_carteira);

        initViews();
        setupNavigation();

        // 1. Configurar Retrofit
        service = RetrofitClient_professor.getRetrofitInstance().create(SupabaseService.class);

        // 2. Carregar Dados
        carregarDadosDoSupabase();

        // 3. AÇÃO DO BOTÃO ADICIONAR (Abre o Menu Deslizante)
        fabAdd.setOnClickListener(v -> {
            showDialogEscolha();
        });
    }

    private void initViews() {
        // Vincular IDs do XML
        recyclerView = findViewById(R.id.recycler_movimentos);
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
        // Define estado inicial (Carteira Ativa)
        updateMenuColors(false);

        // Botão Home -> Vai para TelaInicial
        btnHome.setOnClickListener(v -> {
            updateMenuColors(true);
            Intent intent = new Intent(TelaCarteira.this, TelaInicial.class);
            startActivity(intent);
            finish();
        });

        // Botão Carteira -> Recarrega os dados
        btnWallet.setOnClickListener(v -> {
            updateMenuColors(false);
            carregarDadosDoSupabase();
            Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        });
    }

    // --- MÉTODOS DE BANCO DE DADOS (SUPABASE) ---

    private void carregarDadosDoSupabase() {
        listaUnificada.clear();

        // A. Buscar Receitas
        service.listarReceita(API_KEY, "Bearer " + API_KEY).enqueue(new Callback<List<ReceitaModel>>() {
            @Override
            public void onResponse(Call<List<ReceitaModel>> call, Response<List<ReceitaModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ReceitaModel r : response.body()) {
                        String desc = (r.descricao != null) ? r.descricao : "Receita";
                        String data = (r.data != null) ? r.data : "--/--";
                        listaUnificada.add(new Dados(r.id, desc, r.valor, data, "RECEITA"));
                    }
                    buscarDespesas(); // Sucesso -> Busca Despesas
                } else {
                    buscarDespesas(); // Erro -> Tenta buscar Despesas mesmo assim
                }
            }

            @Override
            public void onFailure(Call<List<ReceitaModel>> call, Throwable t) {
                buscarDespesas(); // Falha -> Tenta buscar Despesas mesmo assim
            }
        });
    }

    private void buscarDespesas() {
        // B. Buscar Despesas
        service.listarDespesa(API_KEY, "Bearer " + API_KEY).enqueue(new Callback<List<DespesaModel>>() {
            @Override
            public void onResponse(Call<List<DespesaModel>> call, Response<List<DespesaModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (DespesaModel d : response.body()) {
                        String desc = (d.descricao != null) ? d.descricao : "Despesa";
                        String data = (d.data != null) ? d.data : "--/--";
                        listaUnificada.add(new Dados(d.id, desc, d.valor, data, "DESPESA"));
                    }
                    atualizarInterface(); // Fim -> Atualiza a tela
                }
            }

            @Override
            public void onFailure(Call<List<DespesaModel>> call, Throwable t) {
                atualizarInterface(); // Atualiza com o que tiver
            }
        });
    }

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

        DadosAdapter adapter = new DadosAdapter(listaUnificada);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



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
            Intent intent = new Intent(TelaCarteira.this, TelaAdicionarReceita.class);
            startActivity(intent);
        });

        // Clique na opção DESPESA
        LinearLayout btnDespesa = view.findViewById(R.id.btn_escolha_despesa);
        btnDespesa.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(TelaCarteira.this, TelaAdicionarDespesas.class);
            startActivity(intent);
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