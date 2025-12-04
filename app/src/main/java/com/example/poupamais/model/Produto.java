package com.example.poupamais.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private Integer id;
    private String nome;
    private Double preco;

    public Produto(String nome, Double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(int id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Construtor vazio obrigatório para Retrofit/Gson
    public Produto() {}

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public Double getPreco() { return preco; }

    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(Double preco) { this.preco = preco; }
}
