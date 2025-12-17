package com.example.poupamais.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poupamais.model.DespesaModel;
import com.example.poupamais.model.ReceitaModel;
import com.example.poupamais.repository.DadosRepository;

import java.util.ArrayList;
import java.util.Collections;
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

    // 🔹 HOME - mostrar apenas 5 DESPESAS
    public LiveData<List<DespesaModel>> getUltimas5Despesas() {
        MutableLiveData<List<DespesaModel>> ultimas = new MutableLiveData<>();

        despesaModel.observeForever(lista -> {
            if (lista == null || lista.isEmpty()) {
                ultimas.setValue(new ArrayList<>());
                return;
            }

            // 🔹 Ordena da mais recente para a mais antiga
            Collections.sort(lista, (d1, d2) ->
                    d2.getId() - d1.getId()
            );

            int limite = Math.min(lista.size(), 5);
            ultimas.setValue(new ArrayList<>(lista.subList(0, limite)));
        });

        return ultimas;
    }


    // 🔹 HOME - mostrar apenas 5 RECEITAS
    public LiveData<List<ReceitaModel>> getUltimas5Receitas() {
        MutableLiveData<List<ReceitaModel>> resultado = new MutableLiveData<>();

        receitaModel.observeForever(lista -> {
            if (lista == null || lista.isEmpty()) {
                resultado.setValue(new ArrayList<>());
                return;
            }

            // 🔹 Ordena da mais recente para a mais antiga
            Collections.sort(lista, (r1, r2) ->
                    r2.getId() - r1.getId()
            );

            int limite = Math.min(lista.size(), 5);
            resultado.setValue(new ArrayList<>(lista.subList(0, limite)));
        });

        return resultado;
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
