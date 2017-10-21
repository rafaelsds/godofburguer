package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ListagemInsumosActivity extends AppCompatActivity {

    private ListView listViewInsumos;
    private FloatingActionButton bttAddInsumo;
    private AlertDialog alerta;
    private String insumoExcluir;

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
        bttAddInsumo = (FloatingActionButton)findViewById(R.id.bttAddInsumo);
    }

    public void botoes(){

        bttAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemInsumosActivity.this, InsumosActivity.class);
                startActivity(it);
                finish();
            }
        });

        listViewInsumos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listViewInsumos.getItemAtPosition(position);
                alert_opcoes_list(o.toString());
            }
        });
    }

    public interface CallBack<T>{
        public void call();
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

            @Override
            public void call(){
            };

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


    public void excluirInsumo(){

        excluirInsumo(new CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluirInsumo(final CallBack callback) {


        if(insumoExcluir != null && !insumoExcluir.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InsumosController controler = retrofit.create(InsumosController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("nome", insumoExcluir);

            Call<Boolean> request = controler.excluir_insumo(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemInsumosActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo Insumo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemInsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        insumoExcluir="";
                        atualizaInsumos();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemInsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void alert_opcoes_list(final String descricaoP) {



        View viewOpcoesCard = getLayoutInflater().inflate(R.layout.opcoes_list,null);

        viewOpcoesCard.findViewById(R.id.bttExcluirOpcoesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                insumoExcluir=descricaoP;
                excluirInsumo();
                alerta.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ListagemInsumosActivity.this);
        builder.setView(viewOpcoesCard);
        alerta = builder.create();
        alerta.show();

    }


}
