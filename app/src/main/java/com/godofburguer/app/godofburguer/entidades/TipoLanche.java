package com.godofburguer.app.godofburguer.entidades;


import java.io.Serializable;

public class TipoLanche implements Serializable{

    private String nome, id;

    public TipoLanche() {}

    public TipoLanche(String nome, String id) {
        this.nome = nome;
        this.id = id;
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

    @Override
    public String toString() {
        return nome;
    }

}
