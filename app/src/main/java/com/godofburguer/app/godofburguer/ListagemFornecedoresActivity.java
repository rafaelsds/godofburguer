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

import com.godofburguer.app.godofburguer.controller.FornecedoresController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;
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

public class ListagemFornecedoresActivity extends Activity {

    private ListView listViewFornecedores;
    private FloatingActionButton bttAddFornecedor;
    private String excluirFornecedor;

    private AlertDialog alerta;

    List<Fornecedores> listFornecedores = new ArrayList<Fornecedores>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_fornecedores);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        listViewFornecedores = (ListView)findViewById(R.id.listaFornecedores);
        bttAddFornecedor = (FloatingActionButton)findViewById(R.id.bttAddFornecedor);
    }

    public void botoes(){

        bttAddFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemFornecedoresActivity.this, FornecedoresActivity.class);
                startActivity(it);
                finish();
            }
        });

        listViewFornecedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listViewFornecedores.getItemAtPosition(position);
                alert_opcoes_list(o.toString());
            }
        });

    }

    public void atualizar() {
        obter(new ListagemFornecedoresActivity.CallBack<List<Fornecedores>>(){
            @Override
            public void call(List<Fornecedores> objeto) {
                List<Fornecedores> list = new ArrayList<Fornecedores>();

                for(Fornecedores r : objeto){
                    list.add(new Fornecedores(r.getNome(),r.getEndereco(), r.getTelefone(),r.getEmail(),r.getId()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemFornecedoresActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<Fornecedores> arrayAdapter = new ArrayAdapter<Fornecedores>(ListagemFornecedoresActivity.this, android.R.layout.simple_list_item_1, list);
                listViewFornecedores.setAdapter(arrayAdapter);

            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemFornecedoresActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornecedoresController controler = retrofit.create(FornecedoresController.class);

        Call<List<Fornecedores>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemFornecedoresActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Fornecedores>>() {
            @Override
            public void onResponse(Call<List<Fornecedores>> call, Response<List<Fornecedores>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemFornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Fornecedores>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemFornecedoresActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemFornecedoresActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemFornecedoresActivity.CallBack callback) {

        if(excluirFornecedor != null && !excluirFornecedor.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FornecedoresController controler = retrofit.create(FornecedoresController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("nome", excluirFornecedor);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemFornecedoresActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemFornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirFornecedor="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemFornecedoresActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void alert_opcoes_list(final String descricaoP) {

        View viewOpcoesCard = getLayoutInflater().inflate(R.layout.opcoes_list,null);

        viewOpcoesCard.findViewById(R.id.bttExcluirOpcoesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                excluirFornecedor=descricaoP;
                excluir();
                alerta.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ListagemFornecedoresActivity.this);
        builder.setView(viewOpcoesCard);
        alerta = builder.create();
        alerta.show();

    }

}
