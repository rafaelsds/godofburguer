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

import com.github.fabtransitionactivity.SheetLayout;
import com.godofburguer.app.godofburguer.controller.InsumosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.Insumos;

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

public class ListagemInsumosActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddInsumo;
    private SheetLayout mSheetLayout;
    private String insumoExcluir;

    private static final int REQUEST_CODE = 1;

    List<Insumos>listInsumos = new ArrayList<Insumos>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_insumos);

        inicialise();
        botoes();

        atualizaInsumos();

    }


    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewInsumos);
        bttAddInsumo = (FloatingActionButton)findViewById(R.id.bttAddInsumo);
        mSheetLayout = (SheetLayout)findViewById(R.id.bottom_sheet_insumos);
        mSheetLayout.setFab(bttAddInsumo);
        mSheetLayout.setFabAnimationEndListener(this);
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, InsumosActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
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

        bttAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });
        
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }
    
    
    public void atualizaInsumos() {
        obterInsumos(new CallBack <List<Insumos>>(){
            @Override
            public void call(List<Insumos> objeto) {
                List<Insumos> list = new ArrayList<Insumos>();

                for(Insumos r : objeto){
                    list.add(new Insumos(r.getNome(),r.getId()));
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(ListagemInsumosActivity.this));
                recyclerView.setAdapter(new ListagemInsumosActivity.NotesAdapter(ListagemInsumosActivity.this,list));

            }

            @Override
            public void call(){
            };

        });
    }


    public void obterInsumos(final CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InsumosController controler = retrofit.create(InsumosController.class);


        Call<List<Insumos>> request = controler.list();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemInsumosActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando Insumos....");

        progressDoalog.show();


        request.enqueue(new Callback<List<Insumos>>() {
            @Override
            public void onResponse(Call<List<Insumos>> call, Response<List<Insumos>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemInsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Insumos>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemInsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void excluirInsumo(){

        excluirInsumo(new CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluirInsumo(final CallBack callback) {


        if(insumoExcluir != null && !insumoExcluir.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InsumosController controler = retrofit.create(InsumosController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("id", insumoExcluir);

            Call<Boolean> request = controler.excluir_insumo(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemInsumosActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo Insumo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemInsumosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        insumoExcluir="";
                        atualizaInsumos();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemInsumosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public class NotesAdapter extends RecyclerView.Adapter<ListagemInsumosActivity.NotesAdapter.ViewHolder> {

        private List<Insumos> mNotes;
        private Context mContext;
        AlertDialog alerta;


        public NotesAdapter(Context context, List<Insumos> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public ListagemInsumosActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_insumos, parent, false);

            ListagemInsumosActivity.NotesAdapter.ViewHolder viewHolder = new ListagemInsumosActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(ListagemInsumosActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final Insumos notes = mNotes.get(position);

            TextView id = viewHolder.id;
            id.setText(notes.getId());

            TextView nome = viewHolder.nome;
            nome.setText(notes.getNome());


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


        public void alerta(final Insumos u){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListagemInsumosActivity.this);

            builder.setTitle("Cadastro de Insumos");
            builder.setMessage("Escolha uma opção:");

            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent it = new Intent(ListagemInsumosActivity.this, InsumosActivity.class);
                    it.putExtra("insumo", u);
                    startActivity(it);
                    finish();
                }
            });

            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    insumoExcluir = u.getId();
                    excluirInsumo();
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
            TextView id, nome;
            CardView card;

            public ViewHolder(View itemView) {
                super(itemView);

                id = (TextView)itemView.findViewById(R.id.cardIdInsumo);
                nome = (TextView)itemView.findViewById(R.id.cardDescricaoInsumo);
                card = (CardView)itemView.findViewById(R.id.cardInsumos);
            }
        }
    }


}
