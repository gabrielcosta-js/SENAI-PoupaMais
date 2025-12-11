package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceitaModel implements Serializable {
    @SerializedName("id_receita")
    public int id;

    @SerializedName("descricao")
    public String descricao;

    @SerializedName("Valor")
    public double valor;

    @SerializedName("data") 
    public String data;
}