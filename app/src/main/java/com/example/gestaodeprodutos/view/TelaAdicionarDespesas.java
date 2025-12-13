package com.example.gestaodeprodutos.view;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class TelaAdicionarDespesas extends AppCompatActivity {

    private ImageView btnVoltar;
    private EditText edt_Valor, edt_Data, edt_NomeDespesa, edt_Descricao, edt_FormaPagamento;

    private String categoriaSelecionada = "";
    private Button btnSalvar;
    private LinearLayout btnAnexar;

    private LinearLayout[] categoriasViews;

    private DadosViewModel dadosViewModel; // Classe Intermediadora

    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW"; //  Anon key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adicionar_despesas);

        initViews();
        setupCategoryClicks();   // <<< CONFIGURAR CATEGORIAS AQUI
        setupDatePicker();

        btnVoltar.setOnClickListener(v -> finish());

        btnAnexar.setOnClickListener(v ->
                Toast.makeText(this, "Abrir galeria...", Toast.LENGTH_SHORT).show()
        );

        btnSalvar.setOnClickListener(v -> {

            String valor = edt_Valor.getText().toString();
            String data = edt_Data.getText().toString();
            String nomeDespesa = edt_NomeDespesa.getText().toString();
            String descricaoDespesa = edt_Descricao.getText().toString();
            String formaPagamento = edt_FormaPagamento.getText().toString();

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

            // Pegar o token do usuário e mandar
            String token = getSharedPreferences("APP", MODE_PRIVATE)
                    .getString("TOKEN", "");


            Double valorDespesa = Double.parseDouble(valor);

            dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
            dadosViewModel.init(this);


            dadosViewModel.inserirDespesa(
                    valorDespesa,
                    categoriaSelecionada,
                    data,
                    nomeDespesa,
                    descricaoDespesa,
                    formaPagamento,
                    token
            );

            Toast.makeText(this,
                    "Salvando: " + nomeDespesa + " (" + categoriaSelecionada + ") - R$ " + valor,
                    Toast.LENGTH_LONG).show();

            finish();
        });
    }

    private void initViews() {
        btnVoltar = findViewById(R.id.btn_voltar);

        edt_Valor = findViewById(R.id.edt_Valor);          // CORRETO
        edt_Data = findViewById(R.id.edt_Data);            // CORRETO
        edt_NomeDespesa = findViewById(R.id.edt_NomeDespesa);
        edt_Descricao = findViewById(R.id.edt_Descricao);  // CORRETO
        edt_FormaPagamento = findViewById(R.id.edt_FormaPagamento);

        btnAnexar = findViewById(R.id.btn_anexar);
        btnSalvar = findViewById(R.id.btn_salvar);

        dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);

        categoriasViews = new LinearLayout[] {
                findViewById(R.id.cat_mercado), findViewById(R.id.cat_alimentacao),
                findViewById(R.id.cat_transporte), findViewById(R.id.cat_moradia),
                findViewById(R.id.cat_saude), findViewById(R.id.cat_lazer),
                findViewById(R.id.cat_outros)
        };

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelaInicial.class);
            startActivity(intent);
        });
    }


    private void setupDatePicker() {
        // Ao clicar no campo de data, abre o calendário
        edt_Data.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Formata para dd/mm/aaaa
                        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String mes = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        edt_Data.setText(year1 + "-" + mes + "-" + dia);
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