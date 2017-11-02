package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FornecedoresController {

    @POST("fornecedores")
    Call<List<Fornecedores>> list();


    @POST("inserir_fornecedor")
    Call<Boolean>inserir(@Body HashMap<String, String> param);

    @POST("excluir_fornecedor")
    Call<Boolean> excluir(@Body HashMap<String, String> param);

    @POST("alterar_fornecedor")
    Call<Boolean> update(@Body HashMap<String, String> param);

}
