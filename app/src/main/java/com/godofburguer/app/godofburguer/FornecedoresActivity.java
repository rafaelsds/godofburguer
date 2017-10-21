package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.FornecedoresController;
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

public class FornecedoresActivity extends Activity {

    Button btnGravar;
    EditText edtDescricao, edtEmail, edtTelefone, edtEndereco;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores);

        inicialise();
        botoes();

    }

    public void inicialise(){
        btnGravar = (Button)findViewById(R.id.btnGravarFornecedor);
        edtDescricao = (EditText)findViewById(R.id.editNomefornencedor);
        edtEndereco = (EditText)findViewById(R.id.editEnderecofornencedor);
        edtEmail = (EditText)findViewById(R.id.editEmailfornencedor);
        edtTelefone = (EditText)findViewById(R.id.edittelefonefornencedor);
    }

    public void botoes(){
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(FornecedoresActivity.this);

                if(validaDados(edtDescricao.getText().toString())){
                    inserir();
                }

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

    public void inserir(){
        inserir(new FornecedoresActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }

    public void inserir(final FornecedoresActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornecedoresController controler = retrofit.create(FornecedoresController.class);

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("nome", edtDescricao.getText().toString());
        param.put("endereco", edtEndereco.getText().toString());
        param.put("email", edtEmail.getText().toString());
        param.put("telefone", edtTelefone.getText().toString());

        Call<Boolean> request = controler.inserir(param);


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(FornecedoresActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Inserindo....");
        progressDoalog.show();

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(FornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call();
                    edtDescricao.setText("");

                    Intent it = new Intent(FornecedoresActivity.this, ListagemFornecedoresActivity.class);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(FornecedoresActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
