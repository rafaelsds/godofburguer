package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.Avaliacao;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AvaliacaoController {

    @POST("avaliacao")
    Call<List<Avaliacao>> list();


    @POST("inserir_avaliacao")
    Call<Boolean>insert(@Body HashMap<String, String> param);

    @POST("excluir_avaliacao")
    Call<Boolean> delete(@Body HashMap<String, String> param);

    @POST("alterar_avaliacao")
    Call<Boolean> update(@Body HashMap<String, String> param);

}
