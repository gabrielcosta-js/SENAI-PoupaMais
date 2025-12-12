package com.example.gestaodeprodutos.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestaodeprodutos.R;

import java.util.Calendar;

public class TelaAlterarReceita extends AppCompatActivity {

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
    }

    public static class TelaEditarReceita extends AppCompatActivity {

        private ImageView btnVoltar;
        private EditText edtValor, edtData, edtDescricao, edtDetalhes;
        private Button btnAtualizar;

        private String categoriaSelecionada = "";
        private LinearLayout[] categoriasViews;

        private int receitaId; // ← ID da receita para atualizar no banco

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tela_alterar_receita);

            initViews();
            setupCategoryClicks();
            setupDatePicker();
            receberDados();

            btnVoltar.setOnClickListener(v -> finish());
            btnAtualizar.setOnClickListener(v -> atualizarReceita());
        }

        private void initViews() {
            btnVoltar = findViewById(R.id.btn_voltar);
            edtValor = findViewById(R.id.edt_valor);
            edtData = findViewById(R.id.edt_data);
            edtDescricao = findViewById(R.id.edt_descricao);
            edtDetalhes = findViewById(R.id.edt_detalhes);
            btnAtualizar = findViewById(R.id.btn_salvar);

            categoriasViews = new LinearLayout[]{
                    findViewById(R.id.cat_salario),
                    findViewById(R.id.cat_freelance)

            };
        }

        private void receberDados() {
            receitaId = getIntent().getIntExtra("id", -1);

            edtValor.setText(getIntent().getStringExtra("valor"));
            edtData.setText(getIntent().getStringExtra("data"));
            edtDescricao.setText(getIntent().getStringExtra("descricao"));
            edtDetalhes.setText(getIntent().getStringExtra("detalhes"));

            categoriaSelecionada = getIntent().getStringExtra("categoria");
        }

        private void atualizarReceita() {
            String valor = edtValor.getText().toString();
            String data = edtData.getText().toString();
            String nome = edtDescricao.getText().toString();

            if (valor.isEmpty()) {
                Toast.makeText(this, "Digite o valor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (categoriaSelecionada.isEmpty()) {
                Toast.makeText(this, "Selecione a categoria", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.isEmpty()) {
                Toast.makeText(this, "Selecione a data", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Receita atualizada!", Toast.LENGTH_LONG).show();


            finish();
        }

        private void setupDatePicker() {
            edtData.setOnClickListener(v -> {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        this,
                        (view, y, m, d) -> edtData.setText(d + "/" + (m+1) + "/" + y),
                        year, month, day
                );
                dialog.show();
            });
        }

        private void setupCategoryClicks() {
            configurarClique(findViewById(R.id.cat_salario), "Salário");
            configurarClique(findViewById(R.id.cat_freelance), "Freelance");
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
}