package com.godofburguer.app.godofburguer;

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
import android.widget.TextView;
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;
import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.db.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Clientes;

import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rafael Silva
 */

public class ListagemClientesActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddCliente;
    private SheetLayout mSheetLayout;
    private String excluirCliente;

    private static final int REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_clientes);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewClientes);
        bttAddCliente = (FloatingActionButton)findViewById(R.id.bttAddCliente);
        mSheetLayout = (SheetLayout)findViewById(R.id.bottom_sheet_clientes);
        mSheetLayout.setFab(bttAddCliente);
        mSheetLayout.setFabAnimationEndListener(this);
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, ClientesActivity.class);
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

        bttAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });

    }



    public void atualizar() {
        SincronizaBancoWs.atualizarClientes(new SincronizaBancoWs.CallBack<List<Clientes>>(){
            @Override
            public void call(List<Clientes> objeto){
                recyclerView.setLayoutManager(new LinearLayoutManager(ListagemClientesActivity.this));
                recyclerView.setAdapter(new ListagemClientesActivity.NotesAdapter(ListagemClientesActivity.this,objeto));
            }
        }, ListagemClientesActivity.this);
    }


    public interface CallBack<T>{
        void call();
    }


    public void excluir(){
        excluir(new ListagemClientesActivity.CallBack() {
            @Override
            public void call() {
            }
        });
    }

    public void excluir(final ListagemClientesActivity.CallBack callback) {

        if(excluirCliente != null && !excluirCliente.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ClientesController controler = retrofit.create(ClientesController.class);

            HashMap<String, String> param = new HashMap<>();

            param.put("id", excluirCliente);

            Call<Boolean> request = controler.excluir(param);

            final android.app.AlertDialog progressDoalog = new SpotsDialog(this, R.style.ProgressDialogCustom);

            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        excluirCliente="";
                        atualizar();
                        callback.call();
                        progressDoalog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemClientesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public class NotesAdapter extends RecyclerView.Adapter<ListagemClientesActivity.NotesAdapter.ViewHolder> {

        private List<Clientes> mNotes;
        private Context mContext;
        AlertDialog alerta;


        public NotesAdapter(Context context, List<Clientes> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public ListagemClientesActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_clientes, parent, false);

            ListagemClientesActivity.NotesAdapter.ViewHolder viewHolder = new ListagemClientesActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(ListagemClientesActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final Clientes notes = mNotes.get(position);

            TextView id = viewHolder.id;
            id.setText(notes.getId());

            TextView nome = viewHolder.nome;
            nome.setText(notes.getNome());

            TextView telefone = viewHolder.telefone;
            telefone.setText(notes.getTelefone());


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


        public void alerta(final Clientes u){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListagemClientesActivity.this);

            builder.setTitle("Cadastro de Clientes");
            builder.setMessage("Escolha uma opção:");

            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent it = new Intent(ListagemClientesActivity.this, ClientesActivity.class);
                    it.putExtra("cliente", u);
                    startActivity(it);
                    finish();
                }
            });

            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    excluirCliente = u.getId();
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
            TextView id, nome, telefone;
            CardView card;

            public ViewHolder(View itemView) {
                super(itemView);

                id = (TextView)itemView.findViewById(R.id.cardIdCliente);
                nome = (TextView)itemView.findViewById(R.id.cardNomeCliente);
                telefone= (TextView)itemView.findViewById(R.id.cardTelefoneCliente);
                card = (CardView)itemView.findViewById(R.id.cardClientes);
            }
        }
    }

}
