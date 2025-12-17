package com.example.poupamais.model;

public class Dados {

    private Integer id;
    private String descricao;
    private double valor;
    private String data;
    private String tipo; // "RECEITA" ou "DESPESA"

    public Dados(Integer id, String descricao, double valor, String data, String tipo) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
    }

    public Integer getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public String getData() { return data; }
    public String getTipo() { return tipo; }
}
