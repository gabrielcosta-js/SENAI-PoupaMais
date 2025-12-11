package com.example.gestaodeprodutos.view;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.adapter.DespesaAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TelaInicial extends AppCompatActivity {

    private TextView txtMesAtual;
    private RecyclerView recyclerViewDespesas;
    private Calendar calendarioAtual;

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

        fabAdd.setOnClickListener(v -> {
            Toast.makeText(this, "Abrir Adicionar Despesa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TelaAdicionarDespesas.class);
            startActivity(intent);
        });

        btnWallet.setOnClickListener(v -> {
            Toast.makeText(this, "Abrir Carteira", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TelaAdicionarReceita.class);
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

        atualizarSaldos(4500.00, 2000.00);
        atualizarTela();
    }

    private void atualizarSaldos(double receita, double despesas) {
        TextView txtReceitaValor = findViewById(R.id.txt_total_receitas);
        TextView txtDespesasValor = findViewById(R.id.txt_total_despesas);


        if (txtReceitaValor != null) {
            txtReceitaValor.setText("R$ " + String.format(Locale.getDefault(), "%.2f", receita));
        }

        if (txtDespesasValor != null) {
            txtDespesasValor.setText("R$ " + String.format(Locale.getDefault(), "%.2f", despesas));
        }

        // TODO: Você precisará de lógica para atualizar as porcentagens (+12% este mês)
    }

    /**
     * Altera o mês exibido e recarrega os dados da lista
     */
    private void mudarMes(int delta) {
        calendarioAtual.add(Calendar.MONTH, delta);
        atualizarTela();
    }

    /**
     * Atualiza o nome do mês e carrega a lista de despesas
     */
    private void atualizarTela() {

        String nomeMes = calendarioAtual.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        txtMesAtual.setText(nomeMes);

        List<DespesaModel> despesasDoMes = carregarDespesasSimuladas();

        if (recyclerViewDespesas != null) {

            DespesaAdapter despesaAdapter = new DespesaAdapter(despesasDoMes);
            recyclerViewDespesas.setAdapter(despesaAdapter);
        }
    }

    /**
     * Simula o carregamento de dados (substituir por acesso ao banco de dados real)
     */
    private List<DespesaModel> carregarDespesasSimuladas() {
        List<DespesaModel> lista = new ArrayList<>();

        // Dados de Exemplo (Baseado no seu design)
        lista.add(new DespesaModel(1, "Mercado Extra", -234.50, "05/11", "Mercado"));
        lista.add(new DespesaModel(2, "Starbucks", -18.90, "06/11", "Alimentação"));
        lista.add(new DespesaModel(3, "Uber", -25.00, "06/11", "Transporte"));
        lista.add(new DespesaModel(4, "Netflix", -39.90, "07/11", "Streaming"));
        lista.add(new DespesaModel(5, "Farmácia", -67.30, "08/11", "Saúde"));
        lista.add(new DespesaModel(6, "Salário", 3500.00, "01/11", "Receita")); // Exemplo de Receita

        return lista;
    }
}