package com.godofburguer.app.godofburguer.entidades;


import java.io.Serializable;

public class Fornecedores implements Serializable{

    private String nome, endereco, telefone, email, id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Fornecedores(String nome, String endereco, String telefone, String email, String id) {
        if(nome != null) {
            this.nome = nome;
        }else{
            this.nome = "";
        }

        if(endereco != null) {
            this.endereco = endereco;
        }else{
            this.endereco = "";
        }

        if(telefone != null) {
            this.telefone = telefone;
        }else{
            this.telefone = "";
        }

        if(email != null) {
            this.email = email;
        }else{
            this.email = "";
        }

        if(id != null) {
            this.id = id;
        }else{
            this.id = "";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  nome;
    }
}
