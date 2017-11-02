package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.Lanches;
import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ListagemLanchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddLanche;
    private String excluirLanche;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_lanches);

        inicialise();
        botoes();

        atualizar();

    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewLanches);
        bttAddLanche = (FloatingActionButton)findViewById(R.id.bttAddLanche);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return;
    }


    public void botoes(){

        bttAddLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListagemLanchesActivity.this, LanchesActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void atualizar() {
        obter(new CallBack <List<Lanches>>(){
            @Override
            public void call(List<Lanches> objeto) {
                List<Lanches> list = new ArrayList<Lanches>();

                for(Lanches r : objeto){
                    list.add(new Lanches(r.getNome(),r.getId(),r.getValor()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemLanchesActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(ListagemLanchesActivity.this));
                recyclerView.setAdapter(new ListagemLanchesActivity.NotesAdapter(ListagemLanchesActivity.this,list));

            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemLanchesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controler = retrofit.create(LanchesController.class);

        Call<List<Lanches>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemLanchesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Lanches>>() {
            @Override
            public void onResponse(Call<List<Lanches>> call, Response<List<Lanches>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemLanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Lanches>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemLanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemInsumosActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemInsumosActivity.CallBack callback) {

        if(excluirLanche != null && !excluirLanche.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LanchesController controler = retrofit.create(LanchesController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("id", excluirLanche);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemLanchesActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemLanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirLanche="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemLanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public class NotesAdapter extends RecyclerView.Adapter<ListagemLanchesActivity.NotesAdapter.ViewHolder> {

        private List<Lanches> mNotes;
        private Context mContext;
        AlertDialog alerta;


        public NotesAdapter(Context context, List<Lanches> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public ListagemLanchesActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_lanches, parent, false);

            ListagemLanchesActivity.NotesAdapter.ViewHolder viewHolder = new ListagemLanchesActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(ListagemLanchesActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final Lanches notes = mNotes.get(position);

            TextView id = viewHolder.id;
            id.setText(notes.getId());

            TextView nome = viewHolder.nome;
            nome.setText(notes.getNome());

            TextView valor = viewHolder.valor;
            valor.setText(String.valueOf(notes.getValor()));

            viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Vibrar();
                    alerta(notes);
                    return false;
                }
            });

        }


        private void Vibrar(){
            Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long milliseconds = 50;
            rr.vibrate(milliseconds);
        }


        public void alerta(final Lanches u){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListagemLanchesActivity.this);

            builder.setTitle("Cadastro de Lanches");
            builder.setMessage("Escolha uma opção:");

            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent it = new Intent(ListagemLanchesActivity.this, LanchesActivity.class);
                    it.putExtra("lanche", u);
                    startActivity(it);
                    finish();
                }
            });

            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    excluirLanche = u.getId();
                    excluir();
                }
            });

            alerta = builder.create();
            alerta.show();
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView id, nome, valor;
            CardView card;

            public ViewHolder(View itemView) {
                super(itemView);

                id = (TextView)itemView.findViewById(R.id.cardIdLanche);
                nome = (TextView)itemView.findViewById(R.id.cardDescricaoLanche);
                valor = (TextView)itemView.findViewById(R.id.cardValorLanche);
                card = (CardView)itemView.findViewById(R.id.cardLanches);
            }
        }
    }
    
}
