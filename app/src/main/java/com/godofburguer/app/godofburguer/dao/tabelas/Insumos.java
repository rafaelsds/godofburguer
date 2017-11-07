package com.godofburguer.app.godofburguer.dao.tabelas;

public class Insumos {
    public static final String TABELA = "insumos";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String FORNECEDOR = "nm_fornecedor";

    public void insumos(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key, "
                + DESCRICAO + " text, "
                + FORNECEDOR + " text"
                +")";
    }
}