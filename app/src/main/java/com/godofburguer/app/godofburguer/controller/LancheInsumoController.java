package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.LancheInsumo;
import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LancheInsumoController {

    @POST("lanche_insumo")
    Call<List<LancheInsumo>> buscarInsumos();

    @POST("inserir_lanche_insumo")
    Call<Boolean>inserir(@Body HashMap<String, String> param);


    @POST("excluir_lanche_insumo")
    Call<Boolean> excluir_insumos(@Body HashMap<String, String> param);

}
