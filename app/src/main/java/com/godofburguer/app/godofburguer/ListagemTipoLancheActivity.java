package com.godofburguer.app.godofburguer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;
import com.godofburguer.app.godofburguer.controller.TipoLancheController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.db.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.TipoLanche;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ListagemTipoLancheActivity extends AppCompatActivity
        implements SheetLayout.OnFabAnimationEndListener{

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddTipoLanche;
    private SheetLayout mSheetLayout;
    private String tipoLancheExcluir;

    private static final int REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_tipo_lanche);

        inicialise();
        botoes();

        atualizaTipoLanche();

    }


    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewTipoLanche);
        bttAddTipoLanche = (FloatingActionButton)findViewById(R.id.bttAddTipoLanche);
        mSheetLayout = (SheetLayout)findViewById(R.id.bottom_sheet_tipo_lanche);
        mSheetLayout.setFab(bttAddTipoLanche);
        mSheetLayout.setFabAnimationEndListener(this);
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, TipoLancheActivity.class);
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
    }


    public void botoes(){
        bttAddTipoLanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });
    }

    public interface CallBack<T>{
        void call();
    }


    public void atualizaTipoLanche(){
        SincronizaBancoWs.atualizarTipoLanche(new SincronizaBancoWs.CallBack<List<TipoLanche>>(){
            @Override
            public void call(List<TipoLanche> objeto){
                recyclerView.setLayoutManager(new LinearLayoutManager(ListagemTipoLancheActivity.this));
                recyclerView.setAdapter(new ListagemTipoLancheActivity.NotesAdapter(ListagemTipoLancheActivity.this,objeto));
            }
        }, ListagemTipoLancheActivity.this);
    }


    public void excluirTipoLanche(final CallBack callback) {
        if(tipoLancheExcluir != null && !tipoLancheExcluir.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TipoLancheController controler = retrofit.create(TipoLancheController.class);

            HashMap<String, String> param = new HashMap<>();

            param.put("id", tipoLancheExcluir);

            Call<Boolean> request = controler.excluir_tipo_lanche(param);

            final SweetAlertDialog progressDoalog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            progressDoalog.setTitleText("Carregando...");
            progressDoalog.setCancelable(false);
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemTipoLancheActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        tipoLancheExcluir ="";
                        atualizaTipoLanche();
                        progressDoalog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemTipoLancheActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private class NotesAdapter extends RecyclerView.Adapter<ListagemTipoLancheActivity.NotesAdapter.ViewHolder> {

        private List<TipoLanche> mNotes;
        private Context mContext;
        AlertDialog alerta;


        private NotesAdapter(Context context, List<TipoLanche> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public ListagemTipoLancheActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_tipo_lanche, parent, false);

            ListagemTipoLancheActivity.NotesAdapter.ViewHolder viewHolder = new ListagemTipoLancheActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(ListagemTipoLancheActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final TipoLanche notes = mNotes.get(position);

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


        private void alerta(final TipoLanche u){
            new SweetAlertDialog(ListagemTipoLancheActivity.this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Cadastro de Tipos de Lanche")
                    .setContentText("Escolha uma opção")
                    .setCancelText("Editar")
                    .setConfirmText("Excluir")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            Intent it = new Intent(ListagemTipoLancheActivity.this, TipoLancheActivity.class);
                            it.putExtra("tipo_lanche", u);
                            startActivity(it);
                            finish();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();

                            excluirTipoLanche(new CallBack() {
                                @Override
                                public void call() {
                                }
                            });;
                        }
                    })
                    .show();
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

                id = (TextView)itemView.findViewById(R.id.cardIdTipoLanche);
                nome = (TextView)itemView.findViewById(R.id.cardDescricaoTipoLanche);
                card = (CardView)itemView.findViewById(R.id.cardTipoLanche);
            }
        }
    }


}
