package com.godofburguer.app.godofburguer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.FornecedoresController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;

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

public class FornecedoresActivity extends AppCompatActivity {

    Button btnCancelar,btnGravar;
    EditText edtDescricao, edtEmail, edtTelefone, edtEndereco;
    Intent intent;

    private String idFornecedor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("fornecedor") != null){
            Fornecedores f = (Fornecedores) intent.getSerializableExtra("fornecedor");
            preencheInformacoes(f);
        }else{
            idFornecedor=null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemFornecedoresActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemFornecedoresActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadFornecedor);
        btnGravar = (Button)findViewById(R.id.btnGravarFornecedor);
        edtDescricao = (EditText)findViewById(R.id.editNomefornencedor);
        edtEndereco = (EditText)findViewById(R.id.editEnderecofornencedor);
        edtEmail = (EditText)findViewById(R.id.editEmailfornencedor);
        edtTelefone = (EditText)findViewById(R.id.edittelefonefornencedor);
        intent = getIntent();
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

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(FornecedoresActivity.this, ListagemFornecedoresActivity.class);
                startActivity(it);
                finish();
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

    public void inserir(){
        inserir(new FornecedoresActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }


    public void preencheInformacoes(Fornecedores u){
        if(u.getEmail() != null)
            edtEmail.setText(u.getEmail());

        if(u.getId() != null)
            idFornecedor = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());


        if(u.getTelefone() != null)
            edtTelefone.setText(u.getTelefone());

        if(u.getEndereco() != null)
            edtEndereco.setText(u.getEndereco());
    }


    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("endereco", edtEndereco.getText().toString());
        hashMap.put("email", edtEmail.getText().toString());
        hashMap.put("telefone", edtTelefone.getText().toString());

        return hashMap;

    }


    public void inserir(final FornecedoresActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornecedoresController controler = retrofit.create(FornecedoresController.class);

        HashMap<String, String> param = obterHashUsuario();

        final android.app.AlertDialog progressDoalog = new SpotsDialog(this, R.style.ProgressDialogCustom);
        progressDoalog.show();


        if(idFornecedor != null) {

            param.put("id", idFornecedor);
            idFornecedor = null;

            Call<Boolean> request = controler.update(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(FornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
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

        }else{

            Call<Boolean> request = controler.inserir(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(FornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
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


}
