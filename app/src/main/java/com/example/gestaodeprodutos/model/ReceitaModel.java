package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

public class ReceitaModel {
    @SerializedName("id_receita")
    public int id;

    @SerializedName("descricao")
    public String descricao;

    @SerializedName("Valor")
    public double valor;

    @SerializedName("data") 
    public String data;
}