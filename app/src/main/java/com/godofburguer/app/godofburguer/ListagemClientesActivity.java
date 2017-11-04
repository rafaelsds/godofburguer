package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.godofburguer.app.godofburguer.controller.ClientesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.dao.Dml;
import com.godofburguer.app.godofburguer.dao.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Clientes;
import com.godofburguer.app.godofburguer.entidades.Clientes;

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

public class ListagemClientesActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddCliente;
    private SheetLayout mSheetLayout;
    private String excluirCliente;

    private static final int REQUEST_CODE = 1;

    private static final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ID;
    private static final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.DESCRICAO;
    private static final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TABELA;
    private static final String T_EMAIL = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.EMAIL;
    private static final String T_ENDERECO = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.ENDERECO;
    private static final String T_TELEFONE = com.godofburguer.app.godofburguer.dao.tabelas.Clientes.TELEFONE;

    SincronizaBancoWs ws;
    Dml crud;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_clientes);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        ws = new SincronizaBancoWs(ListagemClientesActivity.this);
        crud = new Dml(ListagemClientesActivity.this);

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
        return;
    }

    public void botoes(){

        bttAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
;        });
        
    }


    public void atualizar() {
        ws.atualizarClientes();

        //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
        String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_TELEFONE};
        Cursor cursor = crud.getAll(T_TABELA, campos, T_ID+" ASC");

        ArrayList<Clientes> list = new ArrayList<Clientes>();

        if(cursor != null) {
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()) {
                    list.add(new Clientes(
                            cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_ENDERECO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_TELEFONE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                    cursor.moveToNext();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Nenhum Lançamento encontrado!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(ListagemClientesActivity.this));
        recyclerView.setAdapter(new ListagemClientesActivity.NotesAdapter(ListagemClientesActivity.this,list));

    }

    public interface CallBack<T>{
        public void call();
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

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("id", excluirCliente);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemClientesActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemClientesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirCliente="";
                        atualizar();
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
