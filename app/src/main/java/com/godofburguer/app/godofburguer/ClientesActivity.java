package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Clientes;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ClientesActivity extends Activity {

    Button btnCancelar,btnGravar;
    EditText edtDescricao, edtEmail, edtTelefone, edtEndereco;
    Intent intent;

    private String idCliente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("cliente") != null){
            Clientes clientes = (Clientes) intent.getSerializableExtra("cliente");
            preencheInformacoes(clientes);
        }else{
            idCliente=null;
        }
        
    }

    public void inicialise(){
        btnGravar = (Button)findViewById(R.id.btnGravarCliente);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadCliente);
        edtDescricao = (EditText)findViewById(R.id.edtNomeCliente);
        edtEndereco = (EditText)findViewById(R.id.edtEnderecoCliente);
        edtEmail = (EditText)findViewById(R.id.edtEmailCliente);
        edtTelefone = (EditText)findViewById(R.id.edtTelefoneCliente);

        intent = getIntent();
    }

    public void botoes(){
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(ClientesActivity.this);

                if(validaDados(edtDescricao.getText().toString())){
                    inserir();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        public void call();
    }

    public void inserir(){
        inserir(new ClientesActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }

    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("endereco", edtEndereco.getText().toString());
        hashMap.put("email", edtEmail.getText().toString());
        hashMap.put("telefone", edtTelefone.getText().toString());

        return hashMap;

    }

    public void inserir(final ClientesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClientesController controler = retrofit.create(ClientesController.class);

        HashMap<String, String> param = obterHashUsuario();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ClientesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Inserindo....");
        progressDoalog.show();

        if(idCliente != null) {

            param.put("id", idCliente);
            idCliente = null;

            Call<Boolean> request = controler.alterar(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        edtDescricao.setText("");

                        Intent it = new Intent(ClientesActivity.this, ListagemClientesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ClientesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{


            Call<Boolean> request = controler.inserir(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        edtDescricao.setText("");

                        Intent it = new Intent(ClientesActivity.this, ListagemClientesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ClientesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void preencheInformacoes(Clientes u){
        if(u.getEmail() != null)
            edtEmail.setText(u.getEmail());

        if(u.getId() != null)
            idCliente = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());


        if(u.getTelefone() != null)
            edtTelefone.setText(u.getTelefone());

        if(u.getEndereco() != null)
            edtEndereco.setText(u.getEndereco());
    }
    
}
