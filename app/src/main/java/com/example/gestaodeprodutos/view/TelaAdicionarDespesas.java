package com.example.gestaodeprodutos.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestaodeprodutos.R;
import java.util.Calendar;

public class TelaAdicionarDespesas extends AppCompatActivity {

    private ImageView btnVoltar;
    private EditText edtValor, edtData, edtDescricao, edtDetalhes, edtPagamento;
    private Button btnSalvar;
    private LinearLayout btnAnexar;

    private String categoriaSelecionada = "";
    private LinearLayout[] categoriasViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adicionar_despesas);

        initViews();
        setupCategoryClicks();
        setupDatePicker(); // Configura o calendário

        btnVoltar.setOnClickListener(v -> finish());

        btnAnexar.setOnClickListener(v ->
                Toast.makeText(this, "Abrir galeria...", Toast.LENGTH_SHORT).show()
        );

        btnSalvar.setOnClickListener(v -> {
            // Coletar dados
            String valor = edtValor.getText().toString();
            String data = edtData.getText().toString();
            String nomeDespesa = edtDescricao.getText().toString(); // "Nome da despesa"

            if (valor.isEmpty()) {
                Toast.makeText(this, "Digite o valor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (categoriaSelecionada.isEmpty()) {
                Toast.makeText(this, "Escolha uma categoria", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.isEmpty()) {
                Toast.makeText(this, "Selecione a data", Toast.LENGTH_SHORT).show();
                return;
            }

            String mensagem = "Salvando: " + nomeDespesa + " (" + categoriaSelecionada + ") - R$ " + valor;
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        });
    }

    private void initViews() {
        btnVoltar = findViewById(R.id.btn_voltar);
        edtValor = findViewById(R.id.edt_valor);

        edtData = findViewById(R.id.edt_data);
        edtDescricao = findViewById(R.id.edt_descricao);
        edtDetalhes = findViewById(R.id.edt_detalhes);
        edtPagamento = findViewById(R.id.edt_pagamento);
        btnAnexar = findViewById(R.id.btn_anexar);

        btnSalvar = findViewById(R.id.btn_salvar);

        categoriasViews = new LinearLayout[]{
                findViewById(R.id.cat_mercado), findViewById(R.id.cat_alimentacao),
                findViewById(R.id.cat_transporte), findViewById(R.id.cat_moradia),
                findViewById(R.id.cat_saude), findViewById(R.id.cat_lazer),
                findViewById(R.id.cat_outros)
        };
    }

    private void setupDatePicker() {
        // Ao clicar no campo de data, abre o calendário
        edtData.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Formata para dd/mm/aaaa
                        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String mes = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        edtData.setText(dia + "/" + mes + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void setupCategoryClicks() {
        configurarClique(findViewById(R.id.cat_mercado), "Mercado");
        configurarClique(findViewById(R.id.cat_alimentacao), "Alimentação");
        configurarClique(findViewById(R.id.cat_transporte), "Transporte");
        configurarClique(findViewById(R.id.cat_moradia), "Moradia");
        configurarClique(findViewById(R.id.cat_saude), "Saúde");
        configurarClique(findViewById(R.id.cat_lazer), "Lazer");
        configurarClique(findViewById(R.id.cat_outros), "Outros");
    }

    private void configurarClique(LinearLayout layout, String nome) {
        layout.setOnClickListener(v -> {
            for (LinearLayout item : categoriasViews) {
                item.setBackgroundResource(R.drawable.bg_category_item);
            }
            layout.setBackgroundResource(R.drawable.bg_category_selected);
            categoriaSelecionada = nome;
        });
    }
}