package com.godofburguer.app.godofburguer.dao;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.R;
import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.FornecedoresController;
import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.UsuariosController;
import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.Lanches;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SincronizaBancoWs {

    public static void atualizarInsumos(final CallBack callback, final Context context){

        final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Insumos.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Insumos.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Insumos.TABELA;

        final AlertDialog progressDoalog = new SpotsDialog(context, R.style.ProgressDialogCustom);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsumosController controlerInsumos = retrofit.create(InsumosController.class);
        Call<List<Insumos>> requestInsumos = controlerInsumos.list();

        progressDoalog.show();

        requestInsumos.enqueue(new Callback<List<Insumos>>() {
            @Override
            public void onResponse(Call<List<Insumos>> call, Response<List<Insumos>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Insumos> objeto = response.body();

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    for(Insumos r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.ID, r.getId());
                        valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.DESCRICAO, r.getNome());

                        dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.TABELA,valores);
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<Insumos> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Insumos(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum registro encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDoalog.dismiss();
                    callback.call(listReturn);
                }
            }

            @Override
            public void onFailure(Call<List<Insumos>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static void atualizarLanches(final CallBack callback, final Context context){
        //Obtendo os dados da tabela
        final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Lanches.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Lanches.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Lanches.TABELA;
        final String T_VALOR = com.godofburguer.app.godofburguer.dao.tabelas.Lanches.VALOR;

        final AlertDialog progressDoalog = new SpotsDialog(context, R.style.ProgressDialogCustom);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controlerLanches = retrofit.create(LanchesController.class);
        Call<List<Lanches>> requestLanches = controlerLanches.list();

        progressDoalog.show();

        requestLanches.enqueue(new Callback<List<Lanches>>() {
            @Override
            public void onResponse(Call<List<Lanches>> call, Response<List<Lanches>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Lanches> objeto = response.body();

                    for(Lanches r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());
                        valores.put(T_VALOR, r.getValor());

                        dml.insert(T_TABELA,valores);
                    }


                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_VALOR};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<Lanches> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Lanches(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID)),
                                        cursor.getFloat(cursor.getColumnIndexOrThrow(T_VALOR))));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum registro encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDoalog.dismiss();
                    callback.call(listReturn);
                }
            }

            @Override
            public void onFailure(Call<List<Lanches>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void atualizarUsuarios(final CallBack callback, final Context context){
        //Obtendo os dados da tabela
        final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ENDERECO;
        final String T_LOGIN = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.LOGIN;
        final String T_SENHA = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.SENHA;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TELEFONE;

        final AlertDialog progressDoalog = new SpotsDialog(context, R.style.ProgressDialogCustom);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosController controlerUsuarios = retrofit.create(UsuariosController.class);
        Call<List<Usuarios>> requestUsuarios = controlerUsuarios.list();

        progressDoalog.show();

        requestUsuarios.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Usuarios> objeto = response.body();

                    for(Usuarios r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());
                        valores.put(T_EMAIL, r.getEmail());
                        valores.put(T_ENDERECO, r.getEndereco());
                        valores.put(T_LOGIN, r.getLogin());
                        valores.put(T_SENHA, r.getSenha());
                        valores.put(T_TELEFONE, r.getTelefone());

                        dml.insert(T_TABELA,valores);
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_LOGIN, T_SENHA, T_TELEFONE};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    final ArrayList<Usuarios> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Usuarios(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ENDERECO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_TELEFONE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_EMAIL)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_LOGIN)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_SENHA)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum registro encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDoalog.dismiss();
                    callback.call(listReturn);

                }
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public static void atualizarFornecedores(final CallBack callback, final Context context) {
        //Obtendo os dados da tabela
        final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.ENDERECO;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.TELEFONE;

        final AlertDialog progressDoalog = new SpotsDialog(context, R.style.ProgressDialogCustom);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornecedoresController controlerFornecedores = retrofit.create(FornecedoresController.class);
        Call<List<Fornecedores>> requestFornecedores = controlerFornecedores.list();

        progressDoalog.show();

        requestFornecedores.enqueue(new Callback<List<Fornecedores>>() {
            @Override
            public void onResponse(Call<List<Fornecedores>> call, Response<List<Fornecedores>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Fornecedores> objeto = response.body();

                    for(Fornecedores r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());
                        valores.put(T_EMAIL, r.getEmail());
                        valores.put(T_ENDERECO, r.getEndereco());
                        valores.put(T_TELEFONE, r.getTelefone());

                        dml.insert(T_TABELA,valores);
                    }

                    final ArrayList<Fornecedores> listReturn = new ArrayList<>();

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_TELEFONE};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Fornecedores(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ENDERECO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_TELEFONE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_EMAIL)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum registro encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    callback.call(listReturn);
                    progressDoalog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Fornecedores>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static void atualizarClientes(final CallBack callback, final Context context){
        //Obtendo os dados da tabela
        final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ENDERECO;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TELEFONE;

        final AlertDialog progressDoalog = new SpotsDialog(context, R.style.ProgressDialogCustom);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClientesController controlerClientes = retrofit.create(ClientesController.class);
        Call<List<Clientes>> requestClientes = controlerClientes.list();

        progressDoalog.show();

        requestClientes.enqueue(new Callback<List<Clientes>>() {
            @Override
            public void onResponse(Call<List<Clientes>> call, Response<List<Clientes>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Clientes> list = response.body();

                    for (Clientes r : list){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());
                        valores.put(T_EMAIL, r.getEmail());
                        valores.put(T_ENDERECO, r.getEndereco());
                        valores.put(T_TELEFONE, r.getTelefone());

                        dml.insert(T_TABELA, valores);

                    }

                    final ArrayList<Clientes> listReturn = new ArrayList<>();
                    String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_TELEFONE};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Clientes(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ENDERECO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_TELEFONE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_EMAIL)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                                cursor.moveToNext();
                            }
                            cursor.close();
                        }else{
                            Toast.makeText(context, "Nenhum registro encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                    callback.call(listReturn);
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Clientes>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface CallBack<T>{
        void call(T callList);
    }


}