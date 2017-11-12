package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.controller.UsuariosController;

import com.godofburguer.app.godofburguer.entidades.Usuarios;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gustavo on 05/10/17.
 * Modify by Rafael on 25/10/2017
 */

public class LoginActivity extends Activity {
    private Button btnLogar;
    private Button btnSair;
    private EditText loginEdit;
    private EditText senhaEdit;
    private SharedPreferences.Editor editor;
    SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inicialise();
        botoes();

    }
    
    public void inicialise(){
        btnLogar = (Button) findViewById(R.id.btnLogar);
        btnSair = (Button) findViewById(R.id.btnSair);

        loginEdit = (EditText) findViewById(R.id.editUser);
        senhaEdit = (EditText) findViewById(R.id.editSenha);

        editor = getSharedPreferences("usuario", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("usuario", MODE_PRIVATE);

        loginEdit.setText(prefs.getString("login",""));

    }


    public void botoes(){

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaLogin(loginEdit.getText().toString(),
                        senhaEdit.getText().toString())){
                    HideSoftkeyBoard.hideSoftKeyboard(LoginActivity.this);
                    logar();
                }
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public boolean validaLogin(String login, String senha) {
        Boolean isValid = true;

        if(login.isEmpty()) {
            loginEdit.setError(getString(R.string.validate_user));
            isValid = false;
        }

        if(senha.isEmpty()) {
            senhaEdit.setError(getString(R.string.validate_senha));
            isValid = false;
        }

        return isValid;
    }


    public void logar() {
        obter(new LoginActivity.CallBack<List<Usuarios>>(){
            @Override
            public void call(List<Usuarios> objeto) {
                String usuarioLogar=null;
                for(Usuarios r : objeto){

                    if(r.getTipo().equals("I") && loginEdit.getText().toString().trim().toUpperCase().equals(r.getLogin().toUpperCase()) && senhaEdit.getText().toString().trim().toUpperCase().equals(r.getSenha().toUpperCase())){
                        usuarioLogar=r.getLogin();
                    }

                }

                if(usuarioLogar != null){
                    editor.putString("login",usuarioLogar);
                    editor.apply();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {

                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Usuário e senha não conferem!")
                            .show();

                    senhaEdit.setText("");
                    senhaEdit.requestFocus();

                }

            }
        });

    }

    public void obter(final LoginActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosController controler = retrofit.create(UsuariosController.class);

        Call<List<Usuarios>> request = controler.list();

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);

        progressDoalog.show();

        request.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        void call(T callList);
    }

}
