package com.godofburguer.app.godofburguer.entidades;



public class Insumos {

    private String nome;

    public Insumos() {}

    public Insumos(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

}
