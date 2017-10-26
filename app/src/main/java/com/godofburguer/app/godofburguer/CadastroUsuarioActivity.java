package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.UsuariosController;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class CadastroUsuarioActivity extends Activity {
    private Button btnCancelar,btnGravar;
    EditText edtDescricao, edtEmail, edtTelefone, edtEndereco, edtSenha, edtLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuarios);

        inicialise();
        botoes();

    }

    public void inicialise(){
        btnGravar = (Button)findViewById(R.id.btnGravarUsuario);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadUsuario);

        edtDescricao = (EditText)findViewById(R.id.edtNome);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtLogin = (EditText)findViewById(R.id.edtLogin);
    }


    public void botoes(){
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(CadastroUsuarioActivity.this);

                if(validaDados(edtDescricao.getText().toString(), edtSenha.getText().toString(), edtLogin.getText().toString())){
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

    public boolean validaDados(String descricao, String senha, String login) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            edtDescricao.setError(getString(R.string.informe_nome));
            isValid = false;
        }

        if(senha.isEmpty()) {
            edtSenha.setError(getString(R.string.informe_senha));
            isValid = false;
        }

        if(login.isEmpty()) {
            edtLogin.setError(getString(R.string.informe_login));
            isValid = false;
        }

        return isValid;
    }

    public void inserir(final CadastroUsuarioActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosController controler = retrofit.create(UsuariosController.class);

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("nome", edtDescricao.getText().toString());
        param.put("endereco", edtEndereco.getText().toString());
        param.put("email", edtEmail.getText().toString());
        param.put("telefone", edtTelefone.getText().toString());
        param.put("login", edtLogin.getText().toString());
        param.put("senha", edtSenha.getText().toString());

        Call<Boolean> request = controler.inserir(param);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Inserindo....");
        progressDoalog.show();

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call();
                    edtDescricao.setText("");

                    Intent it = new Intent(CadastroUsuarioActivity.this, ListagemUsuariosActivity.class);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(CadastroUsuarioActivity.this, "Eerro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public interface CallBack{
        public void call();
    }

    public void inserir(){
        inserir(new CadastroUsuarioActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }

}
