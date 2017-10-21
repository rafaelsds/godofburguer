package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.LanchesController;
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

public class LanchesActivity extends Activity {

    Button btnGravar;
    EditText edtDescricao, edtValor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanches);

        inicialise();
        botoes();

    }

    public void inicialise(){
        btnGravar = (Button)findViewById(R.id.btnGravarLanche);
        edtDescricao = (EditText)findViewById(R.id.editNomeLanche);
        edtValor = (EditText)findViewById(R.id.editValorLanche);
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
    }

    public interface CallBack{
        public void call();
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

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("nome", edtDescricao.getText().toString());
        param.put("valor", edtValor.getText().toString());

        Call<Boolean> request = controler.inserir(param);


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LanchesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Inserindo....");
        progressDoalog.show();

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(LanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call();
                    edtDescricao.setText("");

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
