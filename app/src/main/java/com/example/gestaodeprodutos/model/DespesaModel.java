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
}