package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ListagemInsumosActivity extends Activity {

    private ListView listViewInsumos;
    private FloatingActionButton bttAddInsumo;

    List<Insumos>listInsumos = new ArrayList<Insumos>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_insumos);

        inicialise();
        botoes();

        atualizaInsumos();

    }


    public void inicialise(){
        listViewInsumos = (ListView)findViewById(R.id.listaInsumos);
//        listInsumos.add(new Insumos("Bacon em tiras"));
//        listInsumos.add(new Insumos("Bacon em cubos"));
//        listInsumos.add(new Insumos("Pão com gergelim"));
//        listInsumos.add(new Insumos("Pão hamburguer"));

//        ArrayAdapter<Insumos> arrayAdapter = new ArrayAdapter<Insumos>(this, android.R.layout.simple_list_item_1, listInsumos);
//        listViewInsumos.setAdapter(arrayAdapter);

        bttAddInsumo = (FloatingActionButton)findViewById(R.id.bttAddInsumo);
    }

    public void botoes(){

        bttAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemInsumosActivity.this, InsumosActivity.class);
                startActivity(it);
            }
        });

    }


    public interface CallBack<T>{
        public void call(T callList);
    }
    
    
    public void atualizaInsumos() {
        obterInsumos(new CallBack <List<Insumos>>(){
            @Override
            public void call(List<Insumos> objeto) {
                List<Insumos> list = new ArrayList<Insumos>();

                for(Insumos r : objeto){
                    list.add(new Insumos(r.getNome(),r.getId()));
                }


                ArrayAdapter<Insumos> arrayAdapter = new ArrayAdapter<Insumos>(ListagemInsumosActivity.this, android.R.layout.simple_list_item_1, list);
                listViewInsumos.setAdapter(arrayAdapter);

            }
        });

    }


    public void obterInsumos(final CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsumosController controler = retrofit.create(InsumosController.class);

        Call<List<Insumos>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemInsumosActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando Insumos....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Insumos>>() {
            @Override
            public void onResponse(Call<List<Insumos>> call, Response<List<Insumos>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemInsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Insumos>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemInsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    
}
