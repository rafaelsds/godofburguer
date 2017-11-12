package com.godofburguer.app.godofburguer.entidades;

import java.io.Serializable;

/**
 * Rafael Silva
 */

public class Usuarios implements Serializable{

    private String nome, endereco, telefone, email, login, senha, id, tipo;

    public Usuarios(String nome, String endereco, String telefone, String email, String login, String senha, String id, String tipo) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.id = id;
        this.tipo = tipo;
    }

    public Usuarios(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Usuarios(String nome, String login, String id) {
        this.nome = nome;
        this.login = login;
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuarios(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return  login;
    }
}
