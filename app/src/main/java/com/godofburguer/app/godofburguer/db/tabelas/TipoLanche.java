package com.godofburguer.app.godofburguer.db.tabelas;

public class TipoLanche {
    public static final String TABELA = "tipo_lanche";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";

    public void insumos(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key, "
                + DESCRICAO + " text "
                +")";
    }
}