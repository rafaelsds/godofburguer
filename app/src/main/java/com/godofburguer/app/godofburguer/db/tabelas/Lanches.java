package com.godofburguer.app.godofburguer.db.tabelas;

public class Lanches {
    public static final String TABELA = "lanches";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR = "vl_lanche";
    public static final String TIPO_LANCHE = "ds_tipo_lanche";

    public void lanches(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key, "
                + DESCRICAO + " text, "
                + VALOR + " text, "
                + TIPO_LANCHE + " text"
                +")";
    }

}