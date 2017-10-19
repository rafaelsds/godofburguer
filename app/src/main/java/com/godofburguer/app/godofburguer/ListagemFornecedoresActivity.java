package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.ArrayList;
import java.util.List;

/**
 * Rafael Silva
 */

public class ListagemFornecedoresActivity extends Activity {

    private ListView listViewFornecedores;
    private FloatingActionButton bttAddFornecedor;

    List<Fornecedores> listFornecedores = new ArrayList<Fornecedores>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_fornecedores);

        inicialise();
        botoes();

    }


    public void inicialise(){
        listViewFornecedores = (ListView)findViewById(R.id.listaFornecedores);
        listFornecedores.add(new Fornecedores("Ambev","","",""));
        listFornecedores.add(new Fornecedores("Friboi","","",""));
        listFornecedores.add(new Fornecedores("Perdigão","","",""));


        ArrayAdapter<Fornecedores> arrayAdapter = new ArrayAdapter<Fornecedores>(this, android.R.layout.simple_list_item_1, listFornecedores);
        listViewFornecedores.setAdapter(arrayAdapter);

        bttAddFornecedor = (FloatingActionButton)findViewById(R.id.bttAddFornecedor);
    }

    public void botoes(){

        bttAddFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemFornecedoresActivity.this, FornecedoresActivity.class);
                startActivity(it);
            }
        });

    }

}
