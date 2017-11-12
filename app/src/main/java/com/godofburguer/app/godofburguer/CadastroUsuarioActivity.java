package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.UsuariosController;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

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

public class CadastroUsuarioActivity extends AppCompatActivity {
    private Button btnCancelar,btnGravar;
    EditText edtDescricao, edtEmail, edtTelefone, edtEndereco, edtSenha, edtLogin;
    Intent intentCadastroUsuario;

    private String idUusario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuarios);

        inicialise();
        botoes();

        if(intentCadastroUsuario.getSerializableExtra("usuario") != null){
            Usuarios usuario = (Usuarios) intentCadastroUsuario.getSerializableExtra("usuario");
            preencheUsuario(usuario);
        }else{
            idUusario=null;
        }

    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnGravar = (Button)findViewById(R.id.btnGravarUsuario);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadUsuario);
        edtDescricao = (EditText)findViewById(R.id.edtNome);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtLogin = (EditText)findViewById(R.id.edtLogin);
        intentCadastroUsuario = getIntent();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemUsuariosActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemUsuariosActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return;
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
            public void onClick(View v){
                Intent it = new Intent(CadastroUsuarioActivity.this, ListagemUsuariosActivity.class);
                startActivity(it);
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

    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("endereco", edtEndereco.getText().toString());
        hashMap.put("email", edtEmail.getText().toString());
        hashMap.put("telefone", edtTelefone.getText().toString());
        hashMap.put("login", edtLogin.getText().toString());
        hashMap.put("senha", edtSenha.getText().toString());
        hashMap.put("tipo", "I");
        return hashMap;

    }

    public void inserir(final CadastroUsuarioActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosController controler = retrofit.create(UsuariosController.class);

        HashMap<String, String> param = obterHashUsuario();

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(CadastroUsuarioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        if(idUusario != null) {
            param.put("id",idUusario);
            idUusario=null;

            Call<Boolean> request = controler.alterar(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(CadastroUsuarioActivity.this, ListagemUsuariosActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Call<Boolean> request = controler.inserir(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(CadastroUsuarioActivity.this, ListagemUsuariosActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
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


    public void preencheUsuario(Usuarios u){
        if(u.getEmail() != null)
            edtEmail.setText(u.getEmail());

        if(u.getId() != null)
            idUusario = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

        if(u.getSenha() != null)
            edtSenha.setText(u.getSenha());

        if(u.getTelefone() != null)
            edtTelefone.setText(u.getTelefone());

        if(u.getEndereco() != null)
            edtEndereco.setText(u.getEndereco());

        if(u.getLogin() != null)
            edtLogin.setText(u.getLogin());

    }

}
