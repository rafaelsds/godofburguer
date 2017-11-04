package com.godofburguer.app.godofburguer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Dml {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public Dml(Context context)
    {
        banco = new CriaBanco(context);
    }


    public void insert(String tabela, ContentValues contentValues){
        db = banco.getWritableDatabase();
        db.insert(tabela, null, contentValues);
        db.close();
    }

    public Cursor getAll(String tabela, String[] campos, String ordem){
        Cursor cursor;

        db = banco.getReadableDatabase();

        if(ordem == null || ordem.isEmpty()){
            cursor = db.query(tabela, campos, null, null, null, null, null, null);
        }else{
            cursor = db.query(tabela, campos, null, null, null, null, ordem, null);
        }

        if(cursor!=null){
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }

    public void delete(String tabela, String where){
        db = banco.getWritableDatabase();

        if(where == null || where.isEmpty()){
            db.delete(tabela,null,null);
        }else{
            db.delete(tabela,where,null);
        }


        db.close();
    }


    public void update(String tabela, ContentValues contentValues, String where){
        db = banco.getWritableDatabase();
        if(where == null || where.isEmpty()){
            db.update(tabela, contentValues, null, null);
        }else{
            db.update(tabela, contentValues, where, null);
        }

    }

    public void drop(String tabela){
        db.execSQL("DROP TABLE IF EXISTS" + tabela);
    }

}