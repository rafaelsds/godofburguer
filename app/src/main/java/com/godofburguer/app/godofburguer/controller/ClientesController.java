package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientesController {

    @POST("clientes")
    Call<List<Clientes>> list();


    @POST("inserir_cliente")
    Call<Boolean>inserir(@Body HashMap<String, String> param);

    @POST("excluir_cliente")
    Call<Boolean> excluir(@Body HashMap<String, String> param);

    @POST("alterar_cliente")
    Call<Boolean> alterar(@Body HashMap<String, String> param);


}
