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

public class LoginActivity extends Activity {
    private Button btnEntrar;
    private Button btnSair;
    private EditText loginEdit;
    private EditText senhaEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnSair = (Button) findViewById(R.id.btnSair);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEdit = (EditText) findViewById(R.id.editUser);
                senhaEdit = (EditText) findViewById(R.id.editSenha);

                if (validaLogin(loginEdit.getText().toString(),
                        senhaEdit.getText().toString())){

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
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

    public void sair(View view) {

    }
}
