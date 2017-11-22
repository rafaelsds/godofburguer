package com.godofburguer.app.godofburguer.db.tabelas;

public class Avaliacao {
    public static final String TABELA = "avaliacao";
    public static final String ID = "nr_sequencia";
    public static final String SATISFACAO = "qt_satisfacao";
    public static final String QUALIDADE = "qt_qualidade";
    public static final String AGILIDADE = "qt_agilidade";

    public void Avaliacao(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key, "
                + SATISFACAO + " text, "
                + QUALIDADE + " text, "
                + AGILIDADE + " text "
                +")";
    }
}