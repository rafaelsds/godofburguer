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

import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class LanchesActivity extends AppCompatActivity {

    Button btnCancelar,btnGravar;
    EditText edtDescricao, edtValor;
    Intent intent;

    private String idLanche;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanches);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("lanche") != null){
            Lanches lanches = (Lanches) intent.getSerializableExtra("lanche");
            preencherInformacoes(lanches);
        }else{
            idLanche=null;
        }

    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadLanche);
        btnGravar = (Button)findViewById(R.id.btnGravarLanche);
        edtDescricao = (EditText)findViewById(R.id.editNomeLanche);
        edtValor = (EditText)findViewById(R.id.editValorLanche);
        intent = getIntent();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemLanchesActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemLanchesActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }


    public void botoes(){
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(LanchesActivity.this);

                if(validaDados(edtDescricao.getText().toString(),edtValor.getText().toString())){
                    inserir();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    public interface CallBack{
        void call();
    }

    public boolean validaDados(String descricao, String valor) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            edtDescricao.setError(getString(R.string.informe_descricao));
            isValid = false;
        }

        if(valor.isEmpty()) {
            edtValor.setError(getString(R.string.informe_valor));
            isValid = false;
        }

        return isValid;
    }

    public void inserir(){
        inserir(new LanchesActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }

    public void inserir(final LanchesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controler = retrofit.create(LanchesController.class);

        HashMap<String, String> param = obterHashUsuario();

        final android.app.AlertDialog progressDoalog = new SpotsDialog(this, R.style.ProgressDialogCustom);
        progressDoalog.show();

        if(idLanche != null){

            param.put("id",idLanche);
            idLanche=null;

            Call<Boolean> request = controler.alterar(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(LanchesActivity.this, "Erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Call<Boolean> request = controler.inserir(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(LanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("valor", edtValor.getText().toString());

        return hashMap;

    }

    public void preencherInformacoes(Lanches u){

        if(u.getId() != null)
            idLanche = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

        if(!String.valueOf(u.getValor()).isEmpty())
            edtValor.setText(String.valueOf(u.getValor()));

    }

}
