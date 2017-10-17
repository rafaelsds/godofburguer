package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.ArrayList;
import java.util.List;

/**
 * Rafael Silva
 */

public class ListagemInsumosActivity extends Activity {

    private ListView listViewInsumos;
    private FloatingActionButton bttAddInsumo;

    List<Insumos>listInsumos = new ArrayList<Insumos>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_insumos);

        inicialise();
        botoes();

    }


    public void inicialise(){
        listViewInsumos = (ListView)findViewById(R.id.listaInsumos);
        listInsumos.add(new Insumos("Bacon em tiras"));
        listInsumos.add(new Insumos("Bacon em cubos"));
        listInsumos.add(new Insumos("Pão com gergelim"));
        listInsumos.add(new Insumos("Pão hamburguer"));

        ArrayAdapter<Insumos> arrayAdapter = new ArrayAdapter<Insumos>(this, android.R.layout.simple_list_item_1, listInsumos);
        listViewInsumos.setAdapter(arrayAdapter);

        bttAddInsumo = (FloatingActionButton)findViewById(R.id.bttAddInsumo);
    }

    public void botoes(){

        bttAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemInsumosActivity.this, Insumos.class);
                startActivity(it);
            }
        });

    }

}
