package com.example.poupamais.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poupamais.model.Produto;
import com.example.poupamais.repository.ProdutoRepository;

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

    public void carregarProdutos() {
        repository.listarProdutos(produtos);
    }

    public LiveData<Boolean> inserirProduto(String nome, Double preco) {
        Produto p = new Produto(nome, preco);
        return repository.inserirProduto(p);
    }

    public LiveData<Boolean> atualizarProduto(int id, String nome, Double preco) {
        Produto p = new Produto(id, nome, preco);
        return repository.atualizarProduto(p);
    }

    public LiveData<Boolean> deletarProduto(int id) {
        return repository.deletarProduto(id);
    }
}


