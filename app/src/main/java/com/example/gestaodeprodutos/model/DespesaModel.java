package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

public class DespesaModel {
    @SerializedName("id_despesas")
    public int id;

    @SerializedName("Descricao")
    public String descricao;

    @SerializedName("Valor")
    public double valor;

    @SerializedName("Data")
    public String data;

    @SerializedName("Categoria")
    public String categoria;


    public DespesaModel(int id, String descricao, double valor, String data, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

}