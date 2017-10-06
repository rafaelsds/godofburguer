package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by gustavo on 05/10/17.
 */

public class ListagemInsumosActivity extends Activity {
    private Button btnEntrar;
    private Button btnSair;
    private EditText loginEdit;
    private EditText senhaEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEdit = (EditText) findViewById(R.id.editUser);
                senhaEdit = (EditText) findViewById(R.id.editSenha);

                if (validaLogin(loginEdit.getText().toString(),
                        senhaEdit.getText().toString())){

                    Intent i = new Intent(ListagemInsumosActivity.this, MainActivity.class);
                    startActivity(i);
                }


            }
        });
    }

    public boolean validaLogin(String login, String senha) {
        return true;
    }

    public void sair(View view) {

    }
}
