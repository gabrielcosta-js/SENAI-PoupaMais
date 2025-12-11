package com.example.gestaodeprodutos.model;

import java.io.Serializable;

public class Produto_professor implements Serializable {
    private Integer id;
    private String nome;
    private Double preco;

    public Produto_professor(String nome, Double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Produto_professor(int id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Construtor vazio obrigatório para Retrofit/Gson
    public Produto_professor() {}

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public Double getPreco() { return preco; }

    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(Double preco) { this.preco = preco; }
}
