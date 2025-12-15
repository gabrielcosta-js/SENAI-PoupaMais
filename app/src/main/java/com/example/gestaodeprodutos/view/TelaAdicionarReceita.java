package com.example.gestaodeprodutos.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;

import java.util.Calendar;

public class TelaAdicionarReceita extends AppCompatActivity {

    private ImageView btnVoltar;
    private EditText edtValor, edtData, edtNomeReceita, edtDescricao;
    private Button btnSalvar;

    private String categoriaSelecionada = "";
    private LinearLayout[] categoriasViews;

    private DadosViewModel dadosViewModel;

    private final String API_KEY = "SUA_ANON_KEY_AQUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adicionar_receita);

        initViews();
        setupCategoryClicks();   // 🔥 MESMO PADRÃO DA DESPESA
        setupDatePicker();

        btnVoltar.setOnClickListener(v -> {
            finish();
        });

        btnSalvar.setOnClickListener(v -> {
            salvarReceita();
        });
    }

    private void initViews() {
        btnVoltar = findViewById(R.id.btn_voltar);
        edtValor = findViewById(R.id.edt_valor);
        edtData = findViewById(R.id.edt_data);
        edtNomeReceita = findViewById(R.id.edtNomeReceita);
        edtDescricao = findViewById(R.id.edtDescricao);
        btnSalvar = findViewById(R.id.btn_salvar);

        categoriasViews = new LinearLayout[]{
                findViewById(R.id.cat_salario),
                findViewById(R.id.cat_freelance),
                findViewById(R.id.cat_investimento),
                findViewById(R.id.cat_presente),
                findViewById(R.id.cat_economia),
                findViewById(R.id.cat_outros)
        };

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
        dadosViewModel.init(this);

    }

    private void salvarReceita() {
        String valor = edtValor.getText().toString();
        String data = edtData.getText().toString();
        String nome = edtNomeReceita.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if (valor.isEmpty()) {
            Toast.makeText(this, "Digite o valor da receita", Toast.LENGTH_SHORT).show();
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
        // Pegar o token do usuário e mandar
        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        Double valorReceita = Double.parseDouble(valor);

        // 👉 MESMA IDEIA DA DESPESA
        dadosViewModel.inserirReceita(
                valorReceita,
                categoriaSelecionada,
                data,
                nome,
                descricao,
                token
        );

        Toast.makeText(this,
                "Salvando Receita: " + nome + " (" + categoriaSelecionada + ") - R$ " + valor,
                Toast.LENGTH_LONG).show();

        finish();
    }

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
                    }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupCategoryClicks() {
        configurarClique(findViewById(R.id.cat_salario), "Salário");
        configurarClique(findViewById(R.id.cat_freelance), "Freelance");
        configurarClique(findViewById(R.id.cat_investimento), "Investimento");
        configurarClique(findViewById(R.id.cat_presente), "Presente");
        configurarClique(findViewById(R.id.cat_economia), "Economia");
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
