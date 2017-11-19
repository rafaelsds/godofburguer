package com.godofburguer.app.godofburguer.db.tabelas;

public class LancheInsumo {
    public static final String TABELA = "lanche_insumo";
    public static final String ID = "nr_sequencia";
    public static final String DESCRICAO = "descricao";
    public static final String ID_LANCHE = "nr_seq_lanche";
    public static final String ID_INSUMO = "nr_seq_insumo";

    public void LancheInsumo(){
    }

    public static String createTable(){
        return "CREATE TABLE "+ TABELA +"("
                + ID + " integer primary key, "
                + DESCRICAO + " text, "
                + ID_LANCHE + " text, "
                + ID_INSUMO + " text"
                +")";
    }

}