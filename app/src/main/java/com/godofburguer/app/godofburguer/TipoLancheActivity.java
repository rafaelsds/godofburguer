package com.godofburguer.app.godofburguer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.TipoLancheController;
import com.godofburguer.app.godofburguer.entidades.TipoLanche;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class TipoLancheActivity extends AppCompatActivity {

    private Button btnCancelar,btnGravarInsumo;
    private EditText edtDescricao;
    private AutoCompleteTextView atcFornecedores;

    private Intent intent;
    private String idInsumo;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_lanche);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("tipo_lanche") != null){
            TipoLanche i = (TipoLanche) intent.getSerializableExtra("tipo_lanche");
            preencherInformacoes(i);
        }else{
            idInsumo=null;
        }
    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCardTipoLanche);
        btnGravarInsumo = (Button)findViewById(R.id.btnGravarTipoLanche);
        edtDescricao = (EditText)findViewById(R.id.edtNomeTipoLanche);
        intent = getIntent();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemTipoLancheActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemTipoLancheActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }


    public void botoes(){
        btnGravarInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(TipoLancheActivity.this);

                if(validaDados(edtDescricao.getText().toString())){
                    inserirTipoLanche();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TipoLancheActivity.this, ListagemTipoLancheActivity.class);
                startActivity(it);
                finish();
            }
        });


    }

    public void inserirTipoLanche(){
        inserirTipoLanche(new TipoLancheActivity.CallBack(){
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
        void call();
    }

    public void inserirTipoLanche(final TipoLancheActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TipoLancheController controler = retrofit.create(TipoLancheController.class);

        HashMap<String, String> param = obterHash();

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();


        if(idInsumo != null) {

            param.put("id", idInsumo);
            idInsumo = null;

            Call<Boolean> request = controler.update(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(TipoLancheActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        //callback.call();
                        progressDoalog.dismiss();
                        Intent it = new Intent(TipoLancheActivity.this, ListagemTipoLancheActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(TipoLancheActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Call<Boolean> request = controler.inserir_tipo_lanche(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(TipoLancheActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(TipoLancheActivity.this, ListagemTipoLancheActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(TipoLancheActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    public HashMap<String, String> obterHash(){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("nome", edtDescricao.getText().toString());

        return hashMap;

    }


    public void preencherInformacoes(TipoLanche u){

        if(u.getId() != null)
            idInsumo = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

    }

}
