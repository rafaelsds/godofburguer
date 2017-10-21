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

import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Clientes;

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

public class ListagemClientesActivity extends Activity {

    private ListView listViewClientes;
    private FloatingActionButton bttAddCliente;
    private String excluirCliente;

    private AlertDialog alerta;
    List<Clientes> listClientes = new ArrayList<Clientes>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_clientes);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        listViewClientes = (ListView)findViewById(R.id.listaClientes);
        bttAddCliente = (FloatingActionButton)findViewById(R.id.bttAddCliente);
    }

    public void botoes(){

        bttAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemClientesActivity.this, ClientesActivity.class);
                startActivity(it);
                finish();
            }
;        });

        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listViewClientes.getItemAtPosition(position);
                alert_opcoes_list(o.toString());
            }
        });
        
    }


    public void atualizar() {
        obter(new ListagemClientesActivity.CallBack<List<Clientes>>(){
            @Override
            public void call(List<Clientes> objeto) {
                List<Clientes> list = new ArrayList<Clientes>();

                for(Clientes r : objeto){
                    list.add(new Clientes(r.getNome(),r.getEndereco(), r.getTelefone(),r.getEmail(),r.getId()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemClientesActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<Clientes> arrayAdapter = new ArrayAdapter<Clientes>(ListagemClientesActivity.this, android.R.layout.simple_list_item_1, list);
                listViewClientes.setAdapter(arrayAdapter);

            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemClientesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClientesController controler = retrofit.create(ClientesController.class);

        Call<List<Clientes>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemClientesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Clientes>>() {
            @Override
            public void onResponse(Call<List<Clientes>> call, Response<List<Clientes>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Clientes>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemClientesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemClientesActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemClientesActivity.CallBack callback) {

        if(excluirCliente != null && !excluirCliente.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ClientesController controler = retrofit.create(ClientesController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("nome", excluirCliente);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemClientesActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirCliente="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemClientesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void alert_opcoes_list(final String descricaoP) {

        View viewOpcoesCard = getLayoutInflater().inflate(R.layout.opcoes_list,null);

        viewOpcoesCard.findViewById(R.id.bttExcluirOpcoesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                excluirCliente=descricaoP;
                excluir();
                alerta.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ListagemClientesActivity.this);
        builder.setView(viewOpcoesCard);
        alerta = builder.create();
        alerta.show();

    }

}
