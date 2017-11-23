package com.godofburguer.app.godofburguer.db;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.R;
import com.godofburguer.app.godofburguer.controller.AvaliacaoController;
import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.FornecedoresController;
import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.LancheInsumoController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.TipoLancheController;
import com.godofburguer.app.godofburguer.controller.UsuariosController;
import com.godofburguer.app.godofburguer.entidades.Avaliacao;
import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.LancheInsumo;
import com.godofburguer.app.godofburguer.entidades.Lanches;
import com.godofburguer.app.godofburguer.entidades.TipoLanche;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SincronizaBancoWs {

    public static void atualizarInsumos(final CallBack callback, final Context context){

        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.Insumos.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.Insumos.DESCRICAO;
        final String T_FORNECEDOR = com.godofburguer.app.godofburguer.db.tabelas.Insumos.FORNECEDOR;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.Insumos.TABELA;


        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
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

                    for(Insumos r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());
                        valores.put(T_FORNECEDOR, r.getFornecedor());

                        dml.insert(T_TABELA,valores);
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_FORNECEDOR};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<Insumos> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Insumos(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_FORNECEDOR)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));
                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum insumo encontrado!",
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



    public static void atualizarTipoLanche(final CallBack callback, final Context context){

        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.TipoLanche.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.TipoLanche.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.TipoLanche.TABELA;


        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TipoLancheController controler = retrofit.create(TipoLancheController.class);
        Call<List<TipoLanche>> request= controler.list();

        progressDoalog.show();

        request.enqueue(new Callback<List<TipoLanche>>() {
            @Override
            public void onResponse(Call<List<TipoLanche>> call, Response<List<TipoLanche>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<TipoLanche> objeto = response.body();

                    for(TipoLanche r : objeto){
                        //Inclui no banco
                        ContentValues valores;
                        valores = new ContentValues();
                        valores.put(T_ID, r.getId());
                        valores.put(T_DESCRICAO, r.getNome());

                        dml.insert(T_TABELA,valores);
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<TipoLanche> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new TipoLanche(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));
                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum tipo de lanche encontrado!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDoalog.dismiss();
                    callback.call(listReturn);
                }
            }

            @Override
            public void onFailure(Call<List<TipoLanche>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void atualizarLanches(final CallBack callback, final Context context){
        //Obtendo os dados da tabela
        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.Lanches.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.Lanches.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.Lanches.TABELA;
        final String T_VALOR = com.godofburguer.app.godofburguer.db.tabelas.Lanches.VALOR;
        final String T_TIPO_LANCHE = com.godofburguer.app.godofburguer.db.tabelas.Lanches.TIPO_LANCHE;

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);

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
                        valores.put(T_TIPO_LANCHE, r.getTipoLanche());

                        dml.insert(T_TABELA,valores);
                    }


                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_VALOR, T_TIPO_LANCHE};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<Lanches> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new Lanches(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID)),
                                        cursor.getFloat(cursor.getColumnIndexOrThrow(T_VALOR)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_TIPO_LANCHE))
                                        ));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum lanche encontrado!",
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
        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.ENDERECO;
        final String T_LOGIN = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.LOGIN;
        final String T_SENHA = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.SENHA;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.TELEFONE;
        final String T_TIPO = com.godofburguer.app.godofburguer.db.tabelas.Usuarios.TIPO;

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
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
                        valores.put(T_TIPO, r.getTipo());

                        dml.insert(T_TABELA,valores);
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_LOGIN, T_SENHA, T_TELEFONE, T_TIPO};
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
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_TIPO))));

                                cursor.moveToNext();
                            }

                        }else{
                            Toast.makeText(context, "Nenhum usuário encontrado!",
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
        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.ENDERECO;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.db.tabelas.Fornecedores.TELEFONE;

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
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
                            Toast.makeText(context, "Nenhum fornecedor encontrado!",
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
        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.Clientes.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.Clientes.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.Clientes.TABELA;
        final String T_EMAIL = com.godofburguer.app.godofburguer.db.tabelas.Clientes.EMAIL;
        final String T_ENDERECO = com.godofburguer.app.godofburguer.db.tabelas.Clientes.ENDERECO;
        final String T_TELEFONE = com.godofburguer.app.godofburguer.db.tabelas.Clientes.TELEFONE;

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
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
                            Toast.makeText(context, "Nenhum cliente encontrado!",
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



    public static void atualizarLancheInsumo(final CallBack callback, final Context context, final Lanches l){
        final String T_ID = com.godofburguer.app.godofburguer.db.tabelas.LancheInsumo.ID;
        final String T_DESCRICAO = com.godofburguer.app.godofburguer.db.tabelas.LancheInsumo.DESCRICAO;
        final String T_TABELA = com.godofburguer.app.godofburguer.db.tabelas.LancheInsumo.TABELA;
        final String T_ID_INSUMO = com.godofburguer.app.godofburguer.db.tabelas.LancheInsumo.ID_INSUMO;
        final String T_ID_LANCHE = com.godofburguer.app.godofburguer.db.tabelas.LancheInsumo.ID_LANCHE;

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LancheInsumoController controler = retrofit.create(LancheInsumoController.class);
        Call<List<LancheInsumo>> request= controler.buscarInsumos();

        progressDoalog.show();

        request.enqueue(new Callback<List<LancheInsumo>>() {
            @Override
            public void onResponse(Call<List<LancheInsumo>> call, Response<List<LancheInsumo>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    //Limpar o SQLITE para incluir os registros obtidos via ws
                    dml.delete(T_TABELA,null);

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<LancheInsumo> objeto = response.body();

                    for(LancheInsumo r : objeto){
                        if(r.getIdLanche().equals(l.getId())){
                            //Inclui no banco
                            ContentValues valores;
                            valores = new ContentValues();
                            valores.put(T_ID, r.getId());
                            valores.put(T_DESCRICAO, r.getNome());
                            valores.put(T_ID_INSUMO, r.getIdInsumo());
                            valores.put(T_ID_LANCHE, r.getIdLanche());
                            dml.insert(T_TABELA,valores);
                        }
                    }

                    //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
                    String[] campos =  {T_ID, T_DESCRICAO,T_ID_INSUMO, T_ID_LANCHE};
                    Cursor cursor = dml.getAll(T_TABELA, campos, T_ID+" ASC");

                    ArrayList<LancheInsumo> listReturn = new ArrayList<>();

                    if(cursor != null) {
                        if (cursor.moveToFirst()){
                            while (!cursor.isAfterLast()) {
                                listReturn.add(new LancheInsumo(
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID_LANCHE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(T_ID_INSUMO))));
                                cursor.moveToNext();
                            }

                        }
                    }

                    progressDoalog.dismiss();
                    callback.call(listReturn);
                }
            }

            @Override
            public void onFailure(Call<List<LancheInsumo>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void atualizarAvaliacao(final CallBack2 callback, final Context context){
        final SweetAlertDialog progressDoalog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        final Dml dml = new Dml(context);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AvaliacaoController controler = retrofit.create(AvaliacaoController.class);
        Call<List<Avaliacao>> request= controler.list();

        progressDoalog.show();

        request.enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {

                    //Intância uma nova lista que recebe os dados do response[WS]
                    List<Avaliacao> objeto = response.body();

                    Integer satisfacao =0, qualidade=0, agilidade=0;

                    for(Avaliacao r : objeto){
                        satisfacao = satisfacao+r.getSatisfacao();
                        qualidade = qualidade+r.getQualidade();
                        agilidade = agilidade+r.getAgilidade();
                    }


                    satisfacao = Math.round(satisfacao / objeto.size());
                    qualidade = Math.round(qualidade / objeto.size());
                    agilidade = Math.round(agilidade / objeto.size());

                    HashMap<String, Integer>hash = new HashMap<>();
                    hash.put("satisfacao",satisfacao);
                    hash.put("qualidade",qualidade);
                    hash.put("agilidade",agilidade);

                    progressDoalog.dismiss();
                    callback.call(hash);
                }
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface CallBack<T>{
        void call(T callList);
    }


    public interface CallBack2{
        void call(HashMap<String, Integer> callList);
    }

}