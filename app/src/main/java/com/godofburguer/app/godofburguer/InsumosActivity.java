package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class InsumosActivity extends AppCompatActivity {

    Button btnCancelar,btnGravarInsumo;
    EditText edtDescricao;

    Intent intent;
    private String idInsumo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insumos);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("insumo") != null){
            Insumos i = (Insumos) intent.getSerializableExtra("insumo");
            preencherInformacoes(i);
        }else{
            idInsumo=null;
        }
    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCardInsumo);
        btnGravarInsumo = (Button)findViewById(R.id.btnGravarInsumo);
        edtDescricao = (EditText)findViewById(R.id.edtNomeInsumo);
        intent = getIntent();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemInsumosActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemInsumosActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return;
    }


    public void botoes(){
        btnGravarInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(InsumosActivity.this);

                if(validaDados(edtDescricao.getText().toString())){
                    inserirInsumo();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(InsumosActivity.this, ListagemInsumosActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void inserirInsumo(){
        inserirInsumo(new InsumosActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }


    public boolean validaDados(String descricao) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            edtDescricao.setError(getString(R.string.informe_descricao));
            isValid = false;
        }

        return isValid;
    }

    public interface CallBack{
        public void call();
    }

    public void inserirInsumo(final InsumosActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsumosController controler = retrofit.create(InsumosController.class);

        HashMap<String, String> param = obterHashUsuario();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(InsumosActivity.this);
        progressDoalog.setMax(300);
        progressDoalog.setMessage("carregando....");
        progressDoalog.show();


        if(idInsumo != null) {

            param.put("id", idInsumo);
            idInsumo = null;

            Call<Boolean> request = controler.update(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(InsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        //callback.call();
                        progressDoalog.dismiss();
                        Intent it = new Intent(InsumosActivity.this, ListagemInsumosActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(InsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Call<Boolean> request = controler.inserir_insumo(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(InsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(InsumosActivity.this, ListagemInsumosActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(InsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("nome", edtDescricao.getText().toString());

        return hashMap;

    }


    public void preencherInformacoes(Insumos u){

        if(u.getId() != null)
            idInsumo = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

    }

}
