package com.example.gestaodeprodutos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.Produto_professor;
import com.example.gestaodeprodutos.repository.ProdutoRepository_professor;

import java.util.List;

public class ProdutoViewModel_professor extends ViewModel {

    private final ProdutoRepository_professor repository;
    private final MutableLiveData<List<Produto_professor>> produtos = new MutableLiveData<>();

    public ProdutoViewModel_professor() {
        repository = new ProdutoRepository_professor();
    }

    public LiveData<List<Produto_professor>> getProdutos() {
        return produtos;
    }

    public void carregarProdutos(String token) {
        repository.listarProdutos(produtos, token);
    }

    public LiveData<Boolean> inserirProduto(String nome, Double preco, String token) {
        Produto_professor p = new Produto_professor(nome, preco);
        return repository.inserirProduto(p, token);
    }

    public LiveData<Boolean> atualizarProduto(int id, String nome, Double preco, String token) {
        Produto_professor p = new Produto_professor(id, nome, preco);
        return repository.atualizarProduto(p, token);
    }

    public LiveData<Boolean> deletarProduto(int id, String token) {
        return repository.deletarProduto(id, token);
    }
}


