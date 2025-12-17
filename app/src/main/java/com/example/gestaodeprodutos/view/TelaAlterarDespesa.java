package com.example.gestaodeprodutos.view;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;

public class TelaAlterarDespesa extends AppCompatActivity {

    private EditText edtNome;
    private AutoCompleteTextView edtCategoria;
    private EditText edtData;
    private AutoCompleteTextView edtFormaPagamento;

    private EditText edtDescricao;
    private EditText txtValorDespesa;

    private Button btnAlterarDespesa;
    private Button btnApagarDespesa;

    private ImageView btn_voltar;

    private DadosViewModel dadosViewModel;
    private String token;

    private DespesaModel despesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_alterar_despesa);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarViews();

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this);

        token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        setupCategoriaDropdown();
        setupFormaPagamentoDropdown();
        setupDatePicker();
        carregarDespesa();
        configurarBotoes();

        btn_voltar.setOnClickListener(v -> {
            finish();
        });
    }

    // =========================
    // 🔹 CATEGORIAS (DROPDOWN)
    // =========================
    private void setupCategoriaDropdown() {
        String[] categorias = {
                "Mercado",
                "Transporte",
                "Alimentação",
                "Saúde",
                "Lazer",
                "Moradia",
                "Outros"
        };

        edtCategoria.setOnClickListener(v -> edtCategoria.showDropDown());


        ArrayAdapter<String> adapterCategoria =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        categorias);

        edtCategoria.setAdapter(adapterCategoria);
    }

    // =========================
// 🔹 FORMA DE PAGAMENTO (DROPDOWN)
// =========================
    private void setupFormaPagamentoDropdown() {

        String[] formasPagamento = {
                "Pix",
                "Cartão",
                "Débito",
                "Dinheiro"
        };

        edtFormaPagamento.setOnClickListener(v -> edtFormaPagamento.showDropDown());

        ArrayAdapter<String> adapterFormaPagamento =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        formasPagamento
                );

        edtFormaPagamento.setAdapter(adapterFormaPagamento);
    }


    // =========================
    // 🔹 DATE PICKER
    // =========================
    private void setupDatePicker() {
        edtData.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {

                        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String mes = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        edtData.setText(year1 + "-" + mes + "-" + dia);
                    },
                    year, month, day);

            datePickerDialog.show();
        });
    }

    // =========================
    // 🔹 CARREGAR DESPESA
    // =========================
    private void carregarDespesa() {
        despesa = (DespesaModel) getIntent().getSerializableExtra("DESPESA");

        if (despesa == null) {
            finish();
            return;
        }

        edtNome.setText(despesa.getNome_despesa());
        edtCategoria.setText(despesa.getCategoria(), false);
        edtData.setText(despesa.getData());
        edtDescricao.setText(despesa.getDescricao());
        edtFormaPagamento.setText(despesa.getForma_pagamento(), false);
        txtValorDespesa.setText(String.valueOf(despesa.getValor()));
    }


    // =========================
    // 🔹 BOTÕES
    // =========================
    private void configurarBotoes() {

        btnAlterarDespesa.setOnClickListener(v -> {
            if (despesa == null) return;

            String valorTexto = txtValorDespesa.getText().toString()
                    .replace("R$", "")
                    .replace(",", ".")
                    .trim();

            if (valorTexto.isEmpty()) {
                txtValorDespesa.setError("Informe o valor");
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorTexto);
            } catch (NumberFormatException e) {
                txtValorDespesa.setError("Valor inválido");
                return;
            }

            dadosViewModel.alterarDespesa(
                    despesa.getId(),
                    valor,
                    edtCategoria.getText().toString(),
                    edtData.getText().toString(),
                    edtNome.getText().toString(),
                    edtDescricao.getText().toString(),
                    edtFormaPagamento.getText().toString(),
                    token
            ).observe(this, sucesso -> {
                if (sucesso) {
                    finish();
                }
            });
        });

        btnApagarDespesa.setOnClickListener(v -> {
            if (despesa == null) return;

            dadosViewModel.deletarDespesa(
                    despesa.getId(),
                    token
            ).observe(this, sucesso -> {
                if (sucesso) {
                    finish();
                }
            });
        });
    }


    // =========================
    // 🔹 FINDS
    // =========================
    private void inicializarViews() {
        edtNome = findViewById(R.id.edtAlterarNome);
        edtCategoria = findViewById(R.id.edtAlterarCategoria);
        edtData = findViewById(R.id.edtAlterarData);
        edtFormaPagamento = findViewById(R.id.edtAlterarFormaPagamento);
        edtDescricao = findViewById(R.id.edtAlterarDescricao);
        txtValorDespesa = findViewById(R.id.txtValorDespesa);

        btnAlterarDespesa = findViewById(R.id.btnAlterarDespesa);
        btnApagarDespesa = findViewById(R.id.btnApagarDespesa);
        btn_voltar = findViewById(R.id.btn_voltar);
    }

}
