package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.TipoLanche;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TipoLancheController {

    @POST("tipo_lanche")
    Call<List<TipoLanche>> list();


    @POST("inserir_tipo_lanche")
    Call<Boolean>inserir_tipo_lanche(@Body HashMap<String, String> param);

    @POST("excluir_tipo_lanche")
    Call<Boolean> excluir_tipo_lanche(@Body HashMap<String, String> param);

    @POST("alterar_tipo_lanche")
    Call<Boolean> update(@Body HashMap<String, String> param);

}
