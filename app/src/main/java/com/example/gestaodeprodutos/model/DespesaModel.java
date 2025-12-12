package com.example.gestaodeprodutos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DespesaModel implements Serializable {
    // OS nomes aqui precisa ser Igual ao que tá no Supabase e na mesma ordem
    @SerializedName("id_despesas")
    public Integer id;

    @SerializedName("valor")
    private double valor;
    @SerializedName("categoria")
    private String categoria;

    @SerializedName("data")
    private String data;

    @SerializedName("nome_despesa")
    private String nome_despesa;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("forma_pagamento")
    private String forma_pagamento;

    public DespesaModel(Integer id, double valor, String categoria, String data, String nome_despesa, String descricao, String forma_pagamento) {
        this.id = id;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
        this.nome_despesa = nome_despesa;
        this.descricao = descricao;
        this.forma_pagamento = forma_pagamento;
    }
    public DespesaModel(double valor, String categoria, String data, String nome_despesa, String descricao, String forma_pagamento) {
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
        this.nome_despesa = nome_despesa;
        this.descricao = descricao;
        this.forma_pagamento = forma_pagamento;
    }
    public DespesaModel(){}

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

    public String getNome_despesa() {
        return nome_despesa;
    }

    public void setNome_despesa(String nome_despesa) {
        this.nome_despesa = nome_despesa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }
}