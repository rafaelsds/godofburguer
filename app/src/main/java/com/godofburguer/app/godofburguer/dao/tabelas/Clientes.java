package com.godofburguer.app.godofburguer.dao.tabelas;

public class Clientes {
    public static final String TABELA = "clientes";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String ENDERECO = "endereco";
    public static final String TELEFONE = "telefone";
    public static final String EMAIL = "email";

    public void Clientes(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key,"
                + DESCRICAO + " text,"
                + ENDERECO + " text,"
                + TELEFONE + " text,"
                + EMAIL + " text"
                +")";
    }

}