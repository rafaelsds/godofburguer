package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;

import java.util.ArrayList;
import java.util.List;

/**
 * Rafael Silva
 */

public class ListagemClientesActivity extends Activity {

    private ListView listViewClientes;
    private FloatingActionButton bttAddCliente;

    List<Clientes> listClientes = new ArrayList<Clientes>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_clientes);

        inicialise();
        botoes();

    }


    public void inicialise(){
        listViewClientes = (ListView)findViewById(R.id.listaClientes);
        listClientes.add(new Clientes("Rafael","","",""));
        listClientes.add(new Clientes("Adrial","","",""));
        listClientes.add(new Clientes("Alcino","","",""));


        ArrayAdapter<Clientes> arrayAdapter = new ArrayAdapter<Clientes>(this, android.R.layout.simple_list_item_1, listClientes);
        listViewClientes.setAdapter(arrayAdapter);

        bttAddCliente = (FloatingActionButton)findViewById(R.id.bttAddCliente);
    }

    public void botoes(){

        bttAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemClientesActivity.this, ClientesActivity.class);
                startActivity(it);
            }
        });

    }

}
