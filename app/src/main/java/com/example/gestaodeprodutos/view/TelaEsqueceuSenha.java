package com.example.gestaodeprodutos.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestaodeprodutos.R;

public class TelaEsqueceuSenha extends AppCompatActivity {

    private EditText edtEsqueceuSenhaEmail;
    private Button btnEsqueceuSenhaEnviarEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_esqueceu_senha);

        // Configuração de insets (somente isso!)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // INICIALIZAÇÃO DAS VIEWS
        edtEsqueceuSenhaEmail = findViewById(R.id.edtEsqueceuSenhaEmail);
        btnEsqueceuSenhaEnviarEmail = findViewById(R.id.btnEsqueceuSenhaEnviarEmail);

        //  BOTÃO: ENVIAR E-MAIL
        btnEsqueceuSenhaEnviarEmail.setOnClickListener(v -> {
            String email = edtEsqueceuSenhaEmail.getText().toString();

            if (email.isEmpty()) {
                edtEsqueceuSenhaEmail.setError("Informe seu e-mail");
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("E-mail enviado")
                    .setMessage("Enviamos um link para redefinir sua senha. Verifique sua caixa de entrada.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        finish(); // Sai da tela somente quando o usuário apertar OK
                    })
                    .show();
        });
    }
}
