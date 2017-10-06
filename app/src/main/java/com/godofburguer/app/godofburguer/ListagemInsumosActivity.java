package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.godofburguer.app.godofburguer.entidades.Insumos;

import java.util.ArrayList;
import java.util.List;

/**
 * Rafael Silva
 */

public class ListagemInsumosActivity extends Activity {

    private ListView listViewInsumos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_insumos);

        listViewInsumos = (ListView)findViewById(R.id.listaInsumos);

        List<Insumos>list = new ArrayList<Insumos>();

        list.add(new Insumos("Bacon em tiras"));
        list.add(new Insumos("Bacon em cubos"));
        list.add(new Insumos("Pão com gergelim"));
        list.add(new Insumos("Pão hamburguer"));

        ArrayAdapter<Insumos> arrayAdapter = new ArrayAdapter<Insumos>(this, android.R.layout.simple_list_item_1, list);
        listViewInsumos.setAdapter(arrayAdapter);

    }


}
