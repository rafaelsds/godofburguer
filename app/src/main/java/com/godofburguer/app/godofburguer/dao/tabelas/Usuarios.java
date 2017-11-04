package com.godofburguer.app.godofburguer.dao.tabelas;

public class Usuarios {
    public static final String TABELA = "usuarios";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String ENDERECO = "endereco";
    public static final String TELEFONE = "telefone";
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";
    public static final String SENHA = "senha";

    public void Usuarios(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key,"
                + DESCRICAO + " text,"
                + ENDERECO + " text,"
                + TELEFONE + " text,"
                + EMAIL + " text,"
                + LOGIN + " text,"
                + SENHA + " text"
                +")";
    }

}