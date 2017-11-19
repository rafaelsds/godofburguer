package com.godofburguer.app.godofburguer.entidades;


import java.io.Serializable;

public class Insumos implements Serializable{

    private String nome, id, fornecedor;

    public Insumos() {}

    public Insumos(String fornecedor, String nome, String id) {
        this.nome = nome;
        this.id = id;
        this.fornecedor = fornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return nome;
    }

}
