package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.RootController;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class InsumosActivity extends Activity {

    Button btnGravarInsumo;
    EditText edtDescricao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insumos);

        inicialise();
        botoes();
    }

    public void inicialise(){
        btnGravarInsumo = (Button)findViewById(R.id.btnGravarInsumo);
        edtDescricao = (EditText)findViewById(R.id.edtNomeInsumo);
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

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("nome", edtDescricao.getText().toString());

        Call<Boolean> request = controler.inserir_insumo(param);


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(InsumosActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Inserindo Insumo....");
        progressDoalog.show();

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(InsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call();
                    edtDescricao.setText("");

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
