package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.ArrayList;
import java.util.List;

/**
 * Rafael Silva
 */

public class ListagemLanchesActivity extends Activity {

    private ListView listViewLanches;
    private FloatingActionButton bttAddLanche;

    List<Lanches> listLanches = new ArrayList<Lanches>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_lanches);

        inicialise();
        botoes();

    }


    public void inicialise(){
        listViewLanches = (ListView)findViewById(R.id.listaLanches);
        listLanches.add(new Lanches("Hamburguer",25));
        listLanches.add(new Lanches("Refrigerante",20));
        listLanches.add(new Lanches("Fritas",15));


        ArrayAdapter<Lanches> arrayAdapter = new ArrayAdapter<Lanches>(this, android.R.layout.simple_list_item_1, listLanches);
        listViewLanches.setAdapter(arrayAdapter);

        bttAddLanche = (FloatingActionButton)findViewById(R.id.bttAddLanche);
    }

    public void botoes(){

        bttAddLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemLanchesActivity.this, LanchesActivity.class);
                startActivity(it);
            }
        });

    }

}
