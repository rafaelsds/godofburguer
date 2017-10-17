package com.godofburguer.app.godofburguer.entidades;



public class Lanches {

    private String nome;
    private float valor;

    @Override
    public String toString() {
        return nome ;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Lanches(String nome, float valor) {
        this.nome = nome;
        this.valor = valor;
    }
}
