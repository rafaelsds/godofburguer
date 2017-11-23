package com.godofburguer.app.godofburguer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.godofburguer.app.godofburguer.db.CriaBanco;
import com.godofburguer.app.godofburguer.db.Dml;
import com.godofburguer.app.godofburguer.db.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Indicador;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private AdapterCardIndicadores card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Dml c = new Dml(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        card = new AdapterCardIndicadores();
        carregarCard();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Menu lateral de opções
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lanches) {
            Intent it = new Intent(MainActivity.this, ListagemLanchesActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_insumos) {
            Intent it = new Intent(MainActivity.this, ListagemInsumosActivity.class);
            startActivity(it);
            finish();

        } else if (id == R.id.nav_tipo_lanche) {
            Intent it = new Intent(MainActivity.this, ListagemTipoLancheActivity.class);
            startActivity(it);
            finish();

        }else if (id == R.id.nav_clientes) {
            Intent it = new Intent(MainActivity.this, ListagemClientesActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_fornecedores) {
            Intent it = new Intent(MainActivity.this, ListagemFornecedoresActivity.class);
            startActivity(it);
            finish();
        }else if (id == R.id.nav_usuarios) {
            Intent it = new Intent(MainActivity.this, ListagemUsuariosActivity.class);
            startActivity(it);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void carregarCard(){

        SincronizaBancoWs.atualizarAvaliacao(new SincronizaBancoWs.CallBack2() {
            @Override
            public void call(HashMap<String, Integer> objeto) {

                card.itemSet(getResources().getString(R.string.satisfacao), getResources().getString(R.string.atendimento_cliente), objeto.get("satisfacao"));
                card.itemSet(getResources().getString(R.string.qualidade), getResources().getString(R.string.qualidade_lanche), objeto.get("qualidade"));
                card.itemSet(getResources().getString(R.string.agilidade), getResources().getString(R.string.tempo_entrega), objeto.get("agilidade"));

                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(card);

            }
        },MainActivity.this);

    }

}
