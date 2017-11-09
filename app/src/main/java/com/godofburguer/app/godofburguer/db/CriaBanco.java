package com.godofburguer.app.godofburguer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.godofburguer.app.godofburguer.db.tabelas.Clientes;
import com.godofburguer.app.godofburguer.db.tabelas.Fornecedores;
import com.godofburguer.app.godofburguer.db.tabelas.Insumos;
import com.godofburguer.app.godofburguer.db.tabelas.Lanches;
import com.godofburguer.app.godofburguer.db.tabelas.Usuarios;

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "burguerdb.db";
    private static final int VERSAO = 1;


    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Clientes.createTable());
        db.execSQL(Fornecedores.createTable());
        db.execSQL(Insumos.createTable());
        db.execSQL(Lanches.createTable());
        db.execSQL(Usuarios.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}