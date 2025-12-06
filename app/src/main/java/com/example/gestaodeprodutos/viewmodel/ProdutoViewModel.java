package com.example.gestaodeprodutos.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaodeprodutos.model.Produto;
import com.example.gestaodeprodutos.repository.ProdutoRepository;

import java.util.List;

public class ProdutoViewModel extends ViewModel {

    private final ProdutoRepository repository;
    private final MutableLiveData<List<Produto>> produtos = new MutableLiveData<>();

    public ProdutoViewModel() {
        repository = new ProdutoRepository();
    }

    public LiveData<List<Produto>> getProdutos() {
        return produtos;
    }

    public void carregarProdutos(String token) {
        repository.listarProdutos(produtos, token);
    }

    public LiveData<Boolean> inserirProduto(String nome, Double preco, String token) {
        Produto p = new Produto(nome, preco);
        return repository.inserirProduto(p, token);
    }

    public LiveData<Boolean> atualizarProduto(int id, String nome, Double preco, String token) {
        Produto p = new Produto(id, nome, preco);
        return repository.atualizarProduto(p, token);
    }

    public LiveData<Boolean> deletarProduto(int id, String token) {
        return repository.deletarProduto(id, token);
    }
}


