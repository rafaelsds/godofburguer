package com.godofburguer.app.godofburguer.dao.tabelas;

public class Fornecedores {
    public static final String TABELA = "fornecedores";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String ENDERECO = "endereco";
    public static final String TELEFONE = "telefone";
    public static final String EMAIL = "email";

    public void fornecedores(){
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