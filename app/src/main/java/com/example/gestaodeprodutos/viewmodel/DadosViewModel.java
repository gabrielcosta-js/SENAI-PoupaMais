package com.example.gestaodeprodutos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel; // <-- IMPORT NECESSÁRIO por causa dissp aqui  // INICIALIZAÇÕES
// dadosViewModel = new ViewModelProvider(this).get(DadosViewModel.class);
import com.example.gestaodeprodutos.model.Dados;
import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.Produto_professor;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.repository.DadosRepository;
import com.example.gestaodeprodutos.repository.ProdutoRepository_professor;

import java.util.List;

public class DadosViewModel extends ViewModel {

    // Ligação com a classe
    private final DadosRepository repositoryDespesa;
    private final DadosRepository repositoryReceita;

    private final MutableLiveData<List<DespesaModel>> despesaModel = new MutableLiveData<>();
    private final MutableLiveData<List<ReceitaModel>> receitaModel = new MutableLiveData<>();

    public DadosViewModel() {
        repositoryDespesa = new DadosRepository();
        repositoryReceita = new DadosRepository();
    }

    // Get para despesa e Receita Conecta a mutable despesa model
    public LiveData<List<DespesaModel>> getDespesa() {
        return despesaModel;
    }
    public LiveData<List<ReceitaModel>> getReceita() {
        return receitaModel;
    }

    // Carregar Receita e Despesa
    public void carregarDespesa(String token) {
        repositoryDespesa.listarDespesa(despesaModel, token);
    }
    public void carregarReceita(String token) {
        repositoryReceita.listarReceita(receitaModel, token);
    }


    // Inserir Despesa e Receita
    public LiveData<Boolean> inserirDespesa(double valor, String categoria, String data, String nome_despesa, String descricao, String forma_pagamento, String token) {
        DespesaModel p = new DespesaModel(valor, categoria, data, nome_despesa, descricao, forma_pagamento);
        System.out.println(p.getValor());
        return repositoryDespesa.inserirDespesa(p, token);
    }
    public LiveData<Boolean> inserirReceita(double valor, String categoria, String data, String nomeReceita,String descricao, String token) {
        ReceitaModel p = new ReceitaModel(valor, categoria,data, nomeReceita, descricao);
        return repositoryReceita.inserirReceita(p, token);
    }

    // Atualizar/Alterar Despesa e Receita
    public LiveData<Boolean> alterarDespesa(int id, double valor, String categoria, String data, String nome_despesa, String descricao, String forma_pagamento, String token) {
        DespesaModel p = new DespesaModel(id, valor, categoria, data, nome_despesa, descricao, forma_pagamento);
        return repositoryDespesa.alterarDespesa(p, token);
    }
    public LiveData<Boolean> alterarReceita(int id, double valor, String categoria, String data, String nomeReceita,String descricao, String token) {
        ReceitaModel p = new ReceitaModel(id, valor, categoria,data, nomeReceita, descricao);
        return repositoryReceita.alterarReceita(p, token);
    }

    // Apagar Despesa e Receita
    public LiveData<Boolean> deletarDespesa(int id, String token) {
        return repositoryDespesa.deletarDespesa(id, token);
    }
    public LiveData<Boolean> deletarReceita(int id, String token) {
        return repositoryReceita.deletarReceita(id, token);
    }


    // Usuário
    static Dados dados = new Dados();

    public boolean validarDadosLogin(String email, String senha){
        return email.equals(dados.getEmail()) && senha.equals(dados.getSenha());
    }

    public void setCadastrarUsuario(String nome, String email, String senha) {
        dados = new Dados(nome, email, senha);
    }
}
