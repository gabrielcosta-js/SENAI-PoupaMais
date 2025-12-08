package com.example.gestaodeprodutos.viewmodel;

import androidx.lifecycle.ViewModel; // <-- IMPORT NECESSÁRIO por causa dissp aqui  // INICIALIZAÇÕES
// dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
import com.example.gestaodeprodutos.model.Dados;

public class DadosViewModel extends ViewModel {

    static Dados dados = new Dados();

    public boolean validarDadosLogin(String email, String senha){
        return email.equals(dados.getEmail()) && senha.equals(dados.getSenha());
    }

    public void setCadastrarUsuario(String nome, String email, String senha) {
        dados = new Dados(nome, email, senha);
    }
}
