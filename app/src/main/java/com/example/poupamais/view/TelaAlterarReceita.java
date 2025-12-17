package com.example.poupamais.view;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.poupamais.R;
import com.example.poupamais.model.ReceitaModel;
import com.example.poupamais.viewmodel.DadosViewModel;

public class TelaAlterarReceita extends AppCompatActivity {

    private EditText edtNome;
    private AutoCompleteTextView edtCategoria;
    private EditText edtData;
    private EditText edtDescricao;
    private EditText txtValorReceita;

    private Button btnAlterarReceita;
    private Button btnApagarReceita;
    private ImageView btn_voltar;

    private DadosViewModel dadosViewModel;
    private String token;

    private ReceitaModel receita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_alterar_receita);

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
        setupDatePicker();
        carregarReceita();
        configurarBotoes();

        btn_voltar.setOnClickListener(v -> finish());
    }

    // =========================
    // 🔹 CATEGORIAS (DROPDOWN)
    // =========================
    private void setupCategoriaDropdown() {
        String[] categorias = {
                "Salário",
                "Freelance",
                "Investimentos",
                "Presente",
                "Outros"
        };

        edtCategoria.setOnClickListener(v -> edtCategoria.showDropDown());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        categorias
                );

        edtCategoria.setAdapter(adapter);
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year1, monthOfYear, dayOfMonth) -> {

                        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String mes = (monthOfYear + 1) < 10
                                ? "0" + (monthOfYear + 1)
                                : String.valueOf(monthOfYear + 1);

                        edtData.setText(year1 + "-" + mes + "-" + dia);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    // =========================
    // 🔹 CARREGAR RECEITA
    // =========================
    private void carregarReceita() {
        receita = (ReceitaModel) getIntent().getSerializableExtra("RECEITA");

        if (receita == null) return;

        edtNome.setText(receita.getNome_receita());
        edtCategoria.setText(receita.getCategoria(), false);
        edtData.setText(receita.getData());
        edtDescricao.setText(receita.getDescricao());
        txtValorReceita.setText("R$ " + receita.getValor());
    }

    // =========================
    // 🔹 BOTÕES
    // =========================
    private void configurarBotoes() {

        btnAlterarReceita.setOnClickListener(v -> {
            if (receita == null) return;

            String valorTexto = txtValorReceita.getText().toString()
                    .replace("R$", "")
                    .replace(",", ".")
                    .trim();

            if (valorTexto.isEmpty()) {
                txtValorReceita.setError("Informe o valor");
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorTexto);
            } catch (NumberFormatException e) {
                txtValorReceita.setError("Valor inválido");
                return;
            }

            dadosViewModel.alterarReceita(
                    receita.getId(),
                    valor,
                    edtCategoria.getText().toString(),
                    edtData.getText().toString(),
                    edtNome.getText().toString(),
                    edtDescricao.getText().toString(),
                    token
            ).observe(this, sucesso -> {
                if (sucesso) {
                    finish();
                }
            });
        });

        btnApagarReceita.setOnClickListener(v -> {
            if (receita == null) return;

            dadosViewModel.deletarReceita(
                    receita.getId(),
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
        edtDescricao = findViewById(R.id.edtAlterarDescricao);
        txtValorReceita = findViewById(R.id.txtValorDespesa);

        btnAlterarReceita = findViewById(R.id.btnAlterarDespesa);
        btnApagarReceita = findViewById(R.id.btnApagarDespesa);
        btn_voltar = findViewById(R.id.btn_voltar);
    }
}
