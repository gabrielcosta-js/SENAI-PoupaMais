package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceitaModel implements Serializable {

    @SerializedName("id_receita")
    public Integer id;

    @SerializedName("valor")
    public double valor;
    @SerializedName("categoria")
    public String categoria;

    @SerializedName("data")
    public String data;

    @SerializedName("nome_receita")
    public String nome_receita;

    @SerializedName("descricao")
    public String descricao;

    public ReceitaModel(int id, double valor, String categoria, String data, String nome_receita, String descricao) {
        this.id = id;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
        this.nome_receita = nome_receita;
        this.descricao = descricao;
    }
    public ReceitaModel(double valor, String categoria, String data, String nome_receita, String descricao) {
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
        this.nome_receita = nome_receita;
        this.descricao = descricao;
    }

    public ReceitaModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome_receita() {
        return nome_receita;
    }

    public void setNome_receita(String nome_receita) {
        this.nome_receita = nome_receita;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}