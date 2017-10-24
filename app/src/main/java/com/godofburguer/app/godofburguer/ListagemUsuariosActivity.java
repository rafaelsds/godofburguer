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

import com.godofburguer.app.godofburguer.controller.UsuariosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

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

public class ListagemUsuariosActivity extends Activity {

    private ListView listViewUsuarios;
    private FloatingActionButton bttAddUsuario;
    private String excluirUsuario;

    private AlertDialog alerta;
    List<Usuarios> listUsuarios = new ArrayList<Usuarios>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_usuarios);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        listViewUsuarios = (ListView)findViewById(R.id.listaUsuarios);
        bttAddUsuario = (FloatingActionButton)findViewById(R.id.bttAddUsuario);
    }

    public void botoes(){

        bttAddUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemUsuariosActivity.this, CadastroUsuarioActivity.class);
                startActivity(it);
                finish();
            }
;        });

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listViewUsuarios.getItemAtPosition(position);
                alert_opcoes_list(o.toString());
            }
        });

    }


    public void atualizar() {
        obter(new ListagemUsuariosActivity.CallBack<List<Usuarios>>(){
            @Override
            public void call(List<Usuarios> objeto) {
                List<Usuarios> list = new ArrayList<Usuarios>();

                for(Usuarios r : objeto){
                    list.add(new Usuarios(r.getLogin()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemUsuariosActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<Usuarios> arrayAdapter = new ArrayAdapter<Usuarios>(ListagemUsuariosActivity.this, android.R.layout.simple_list_item_1, list);
                listViewUsuarios.setAdapter(arrayAdapter);

            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemUsuariosActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosController controler = retrofit.create(UsuariosController.class);

        Call<List<Usuarios>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemUsuariosActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemUsuariosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemUsuariosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemUsuariosActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemUsuariosActivity.CallBack callback) {

        if(excluirUsuario != null && !excluirUsuario.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuariosController controler = retrofit.create(UsuariosController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("nome", excluirUsuario);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemUsuariosActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemUsuariosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirUsuario ="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemUsuariosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void alert_opcoes_list(final String descricaoP) {

        View viewOpcoesCard = getLayoutInflater().inflate(R.layout.opcoes_list,null);

        viewOpcoesCard.findViewById(R.id.bttExcluirOpcoesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                excluirUsuario =descricaoP;
                excluir();
                alerta.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ListagemUsuariosActivity.this);
        builder.setView(viewOpcoesCard);
        alerta = builder.create();
        alerta.show();

    }

}
