package com.example.gestaodeprodutos.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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


    private List<DespesaModel> carregarDespesasSimuladas() {
        List<DespesaModel> lista = new ArrayList<>();

        return lista;
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
}