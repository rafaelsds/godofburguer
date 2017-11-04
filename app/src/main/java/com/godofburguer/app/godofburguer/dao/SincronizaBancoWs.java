package com.godofburguer.app.godofburguer.dao;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SincronizaBancoWs {

    private Context context;
    private Dml dml;
    private Retrofit retrofit;

    public SincronizaBancoWs(Context context){
        this.context = context;
        dml = new Dml(context);

        retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void atualizarInsumos() {
        atualizarInsumos(new CallBack<List<Insumos>>(){
            @Override
            public void call(List<Insumos> objeto) {
                //Limpar o SQLITE para incluir os registros obtidos via ws
                dml.delete(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.TABELA,null);

                for(Insumos r : objeto){
                    //Inclui no banco
                    ContentValues valores;
                    valores = new ContentValues();
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.ID, r.getId());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.DESCRICAO, r.getNome());

                    dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Insumos.TABELA,valores);
                }
            }
        });
    }

    public void atualizarInsumos(final CallBack callback) {

        InsumosController controlerInsumos = retrofit.create(InsumosController.class);
        Call<List<Insumos>> requestInsumos = controlerInsumos.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando Insumos....");

        progressDoalog.show();

        requestInsumos.enqueue(new Callback<List<Insumos>>() {
            @Override
            public void onResponse(Call<List<Insumos>> call, Response<List<Insumos>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Insumos>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void atualizarLanches() {
        atualizarLanches(new CallBack<List<Lanches>>(){
            @Override
            public void call(List<Lanches> objeto) {
                //Limpar o SQLITE para incluir os registros obtidos via ws
                dml.delete(com.godofburguer.app.godofburguer.dao.tabelas.Lanches.TABELA,null);

                for(Lanches r : objeto){
                    //Inclui no banco
                    ContentValues valores;
                    valores = new ContentValues();
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Lanches.ID, r.getId());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Lanches.DESCRICAO, r.getNome());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Lanches.VALOR, r.getValor());

                    dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Lanches.TABELA,valores);
                }

            }
        });
    }

    public void atualizarLanches(final CallBack callback) {
        LanchesController controlerLanches = retrofit.create(LanchesController.class);
        Call<List<Lanches>> requestLanches = controlerLanches.list();

        requestLanches.enqueue(new Callback<List<Lanches>>() {
            @Override
            public void onResponse(Call<List<Lanches>> call, Response<List<Lanches>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Lanches>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void atualizarUsuarios() {
        atualizarUsuarios(new CallBack<List<Usuarios>>(){
            @Override
            public void call(List<Usuarios> objeto) {
                //Limpar o SQLITE para incluir os registros obtidos via ws
                dml.delete(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TABELA,null);

                for(Usuarios r : objeto){
                    //Inclui no banco
                    ContentValues valores;
                    valores = new ContentValues();
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ID, r.getId());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.DESCRICAO, r.getNome());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.EMAIL, r.getEmail());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ENDERECO, r.getEndereco());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.LOGIN, r.getLogin());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.SENHA, r.getSenha());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TELEFONE, r.getTelefone());

                    dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TABELA,valores);
                }

            }
        });
    }

    public void atualizarUsuarios(final CallBack callback) {
        UsuariosController controlerUsuarios = retrofit.create(UsuariosController.class);
        Call<List<Usuarios>> requestUsuarios = controlerUsuarios.list();

        requestUsuarios.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void atualizarFornecedores() {
        atualizarFornecedores(new CallBack<List<Fornecedores>>(){
            @Override
            public void call(List<Fornecedores> objeto) {
                //Limpar o SQLITE para incluir os registros obtidos via ws
                dml.delete(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.TABELA,null);

                for(Fornecedores r : objeto){
                    //Inclui no banco
                    ContentValues valores;
                    valores = new ContentValues();
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.ID, r.getId());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.DESCRICAO, r.getNome());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.EMAIL, r.getEmail());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.ENDERECO, r.getEndereco());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.TELEFONE, r.getTelefone());

                    dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Fornecedores.TABELA,valores);
                }

            }
        });
    }

    public void atualizarFornecedores(final CallBack callback) {
        FornecedoresController controlerFornecedores = retrofit.create(FornecedoresController.class);
        Call<List<Fornecedores>> requestFornecedores = controlerFornecedores.list();

        requestFornecedores.enqueue(new Callback<List<Fornecedores>>() {
            @Override
            public void onResponse(Call<List<Fornecedores>> call, Response<List<Fornecedores>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Fornecedores>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void atualizarClientes() {
        atualizarClientes(new CallBack<List<Clientes>>(){
            @Override
            public void call(List<Clientes> objeto) {
                //Limpar o SQLITE para incluir os registros obtidos via ws
                dml.delete(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TABELA,null);

                for(Clientes r : objeto){
                    //Inclui no banco
                    ContentValues valores;
                    valores = new ContentValues();
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ID, r.getId());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.DESCRICAO, r.getNome());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.EMAIL, r.getEmail());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ENDERECO, r.getEndereco());
                    valores.put(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TELEFONE, r.getTelefone());

                    dml.insert(com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TABELA,valores);
                }

            }
        });
    }

    public void atualizarClientes(final CallBack callback) {
        ClientesController controlerClientes = retrofit.create(ClientesController.class);
        Call<List<Clientes>> requestClientes = controlerClientes.list();

        requestClientes.enqueue(new Callback<List<Clientes>>() {
            @Override
            public void onResponse(Call<List<Clientes>> call, Response<List<Clientes>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
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