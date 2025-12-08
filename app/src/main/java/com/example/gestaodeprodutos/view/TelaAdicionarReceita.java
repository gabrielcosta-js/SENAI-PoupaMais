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

public class TelaAdicionarReceita extends AppCompatActivity {

    private ImageView btnVoltar;
    private EditText edtValor, edtData, edtDescricao, edtDetalhes;
    private Button btnSalvar;

    private String categoriaSelecionada = "";
    private LinearLayout[] categoriasViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adicionar_receita);

        initViews();
        setupCategoryClicks();
        setupDatePicker();

        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> salvarReceita());
    }

    private void initViews() {
        btnVoltar = findViewById(R.id.btn_voltar);
        edtValor = findViewById(R.id.edt_valor);
        edtData = findViewById(R.id.edt_data);
        edtDescricao = findViewById(R.id.edt_descricao);
        edtDetalhes = findViewById(R.id.edt_detalhes);
        btnSalvar = findViewById(R.id.btn_salvar);

        // Lista das categorias desta tela
        categoriasViews = new LinearLayout[]{
                findViewById(R.id.cat_salario),
                findViewById(R.id.cat_freelance),
                findViewById(R.id.cat_investimento),
                findViewById(R.id.cat_presente),
                findViewById(R.id.cat_economia),
                findViewById(R.id.cat_outros)
        };
    }

    private void salvarReceita() {
        String valor = edtValor.getText().toString();
        String data = edtData.getText().toString();
        String nome = edtDescricao.getText().toString();

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

        String mensagem = "Salvando Receita: " + nome + " (" + categoriaSelecionada + ") - R$ " + valor;
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

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
                        edtData.setText(dia + "/" + mes + "/" + year1);
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