package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.Lanches;

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

public class ListagemLanchesActivity extends Activity {

    private ListView listViewLanches;
    private FloatingActionButton bttAddLanche;

    private String excluirLanche;
    private AlertDialog alerta;
    List<Lanches> listLanches = new ArrayList<Lanches>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_lanches);

        inicialise();
        botoes();

        atualizar();

    }


    public void inicialise(){
        listViewLanches = (ListView)findViewById(R.id.listaLanches);
//        listLanches.add(new Lanches("Hamburguer",25));
//        listLanches.add(new Lanches("Refrigerante",20));
//        listLanches.add(new Lanches("Fritas",15));


        //ArrayAdapter<Lanches> arrayAdapter = new ArrayAdapter<Lanches>(this, android.R.layout.simple_list_item_1, listLanches);
        //listViewLanches.setAdapter(arrayAdapter);

        bttAddLanche = (FloatingActionButton)findViewById(R.id.bttAddLanche);
    }

    public void botoes(){

        bttAddLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemLanchesActivity.this, LanchesActivity.class);
                startActivity(it);
                finish();
            }
        });

        listViewLanches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listViewLanches.getItemAtPosition(position);
                alert_opcoes_list(o.toString());
            }
        });

    }

    public void atualizar() {
        obter(new CallBack <List<Lanches>>(){
            @Override
            public void call(List<Lanches> objeto) {
                List<Lanches> list = new ArrayList<Lanches>();

                for(Lanches r : objeto){
                    list.add(new Lanches(r.getNome(),r.getId(),r.getValor()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemLanchesActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<Lanches> arrayAdapter = new ArrayAdapter<Lanches>(ListagemLanchesActivity.this, android.R.layout.simple_list_item_1, list);
                listViewLanches.setAdapter(arrayAdapter);

            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemLanchesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controler = retrofit.create(LanchesController.class);

        Call<List<Lanches>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemLanchesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Lanches>>() {
            @Override
            public void onResponse(Call<List<Lanches>> call, Response<List<Lanches>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemLanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Lanches>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemLanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemInsumosActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemInsumosActivity.CallBack callback) {

        if(excluirLanche != null && !excluirLanche.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LanchesController controler = retrofit.create(LanchesController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("nome", excluirLanche);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemLanchesActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemLanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirLanche="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemLanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void alert_opcoes_list(final String descricaoP) {

        View viewOpcoesCard = getLayoutInflater().inflate(R.layout.opcoes_list,null);

        viewOpcoesCard.findViewById(R.id.bttExcluirOpcoesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                excluirLanche=descricaoP;
                excluir();
                alerta.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ListagemLanchesActivity.this);
        builder.setView(viewOpcoesCard);
        alerta = builder.create();
        alerta.show();

    }

}
