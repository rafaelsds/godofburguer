package com.godofburguer.app.godofburguer.entidades;


import java.io.Serializable;

public class LancheInsumo implements Serializable{

    private String nome, id, idLanche, idInsumo;

    public LancheInsumo(String nome, String idLanche, String idInsumo) {
        this.nome = nome;
        this.idLanche = idLanche;
        this.idInsumo = idInsumo;
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

    public String getIdLanche() {
        return idLanche;
    }

    public void setIdLanche(String idLanche) {
        this.idLanche = idLanche;
    }

    public String getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    @Override
    public String toString() {
        return nome;
    }

}
