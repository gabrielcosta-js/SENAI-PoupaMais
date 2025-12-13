package com.example.gestaodeprodutos.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.repository.DadosRepository;

import java.util.List;

public class DadosViewModel extends ViewModel {

    private DadosRepository repository;

    private final MutableLiveData<List<DespesaModel>> despesaModel = new MutableLiveData<>();
    private final MutableLiveData<List<ReceitaModel>> receitaModel = new MutableLiveData<>();

    // 🔹 Inicialização correta
    public void init(Context context) {
        repository = new DadosRepository(context);
    }

    // GETTERS
    public LiveData<List<DespesaModel>> getDespesa() {
        return despesaModel;
    }

    public LiveData<List<ReceitaModel>> getReceita() {
        return receitaModel;
    }

    // CARREGAR
    public void carregarDespesa(String token) {
        repository.listarDespesa(despesaModel, token);
    }

    public void carregarReceita(String token) {
        repository.listarReceita(receitaModel, token);
    }

    // INSERIR
    public LiveData<Boolean> inserirDespesa(
            double valor, String categoria, String data,
            String nomeDespesa, String descricao,
            String formaPagamento, String token
    ) {
        DespesaModel p = new DespesaModel(
                valor, categoria, data, nomeDespesa, descricao, formaPagamento
        );
        return repository.inserirDespesa(p, token);
    }

    public LiveData<Boolean> inserirReceita(
            double valor, String categoria, String data,
            String nomeReceita, String descricao, String token
    ) {
        ReceitaModel p = new ReceitaModel(
                valor, categoria, data, nomeReceita, descricao
        );
        return repository.inserirReceita(p, token);
    }

    // ALTERAR
    public LiveData<Boolean> alterarDespesa(
            int id, double valor, String categoria, String data,
            String nomeDespesa, String descricao,
            String formaPagamento, String token
    ) {
        DespesaModel p = new DespesaModel(
                id, valor, categoria, data, nomeDespesa, descricao, formaPagamento
        );
        return repository.alterarDespesa(p, token);
    }

    public LiveData<Boolean> alterarReceita(
            int id, double valor, String categoria, String data,
            String nomeReceita, String descricao, String token
    ) {
        ReceitaModel p = new ReceitaModel(
                id, valor, categoria, data, nomeReceita, descricao
        );
        return repository.alterarReceita(p, token);
    }

    // DELETAR
    public LiveData<Boolean> deletarDespesa(int id, String token) {
        return repository.deletarDespesa(id, token);
    }

    public LiveData<Boolean> deletarReceita(int id, String token) {
        return repository.deletarReceita(id, token);
    }
}
