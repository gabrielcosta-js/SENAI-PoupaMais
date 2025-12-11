package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DespesaModel implements Serializable {
    @SerializedName("id_despesas")
    public Integer id;

    @SerializedName("Descricao")
    public String descricao;

    @SerializedName("Valor")
    public double valor;

    @SerializedName("Data")
    public String data;

    @SerializedName("Categoria")
    public String categoria;


    public DespesaModel(Integer id, String descricao, double valor, String data, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }
    public DespesaModel(String descricao, String detalhe, double valor, String data, String categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }
    public DespesaModel(String descricao, Double valor, String data, String categoria){}

    public Integer getId() {
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