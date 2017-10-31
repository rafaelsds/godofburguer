package com.godofburguer.app.godofburguer.entidades;


import java.io.Serializable;

public class Lanches implements Serializable{

    private String nome,id;
    private float valor;


    @Override
    public String toString() {
        return nome ;
    }

    public Lanches(String nome, String id, float valor) {
        this.nome = nome;
        this.id = id;
        this.valor = valor;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
