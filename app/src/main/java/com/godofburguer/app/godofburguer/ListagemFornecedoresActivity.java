package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import com.godofburguer.app.godofburguer.entidades.Fornecedores;

/**
 * Created by Alcino on 17-Oct-17.
 */

public class ListagemFornecedoresActivity extends Activity {

    private ListView listViewFornecedores;
    private FloatingActionButton bttAddFornecedores;

    List<Fornecedores> listFornecedores = new ArrayList<Fornecedores>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_fornecedores);

        inicialise();
        botoes();

    }


    public void inicialise(){
        listViewFornecedores = (ListView)findViewById(R.id.listaFornecedores);



        ArrayAdapter<Fornecedores> arrayAdapter = new ArrayAdapter<Fornecedores>(this, android.R.layout.simple_list_item_1, listFornecedores);
        listViewFornecedores.setAdapter(arrayAdapter);

        bttAddFornecedores = (FloatingActionButton)findViewById(R.id.bttAddFornecedores);
    }

    public void botoes(){

        bttAddFornecedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemFornecedoresActivity.this, FornecedoresActivity.class);
                startActivity(it);
            }
        });

    }

}
