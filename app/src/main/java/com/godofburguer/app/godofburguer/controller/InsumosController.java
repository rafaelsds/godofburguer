package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InsumosController {

    @POST("insumos")
    Call<List<Insumos>> list();


    @POST("inserir_insumo")
    Call<Insumos> inserir_insumo(@Body HashMap<String, String> param);

}
