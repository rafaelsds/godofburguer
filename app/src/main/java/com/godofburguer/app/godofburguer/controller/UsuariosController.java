package com.godofburguer.app.godofburguer.controller;


import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuariosController {

    @POST("usuarios")
    Call<List<Usuarios>> list();

    @POST("inserir_usuario")
    Call<Boolean>inserir(@Body HashMap<String, String> param);

    @POST("excluir_usuario")
    Call<Boolean> excluir(@Body HashMap<String, String> param);

    @POST("alterar_usuario")
    Call<Boolean> alterar(@Body HashMap<String, String> param);

}
