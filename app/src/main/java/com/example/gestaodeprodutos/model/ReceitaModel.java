package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceitaModel implements Serializable {

    public ReceitaModel(int id, String descricao, double valor, String data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }
    public ReceitaModel(String descricao, double valor, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public ReceitaModel() {
    }

    @SerializedName("id_receita")
    public Integer id;

    @SerializedName("descricao")
    public String descricao;

    @SerializedName("Valor")
    public double valor;

    @SerializedName("data") 
    public String data;

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
}