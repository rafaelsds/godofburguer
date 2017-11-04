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
import com.godofburguer.app.godofburguer.controller.UsuariosController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.dao.Dml;
import com.godofburguer.app.godofburguer.dao.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Usuarios;

import java.text.SimpleDateFormat;
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

public class ListagemUsuariosActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    private FloatingActionButton bttAddUsuario;
    private String excluirUsuario;
    private SheetLayout mSheetLayout;
    private RecyclerView recyclerView;

    private static final int REQUEST_CODE = 1;

    private static final String T_ID = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ID;
    private static final String T_DESCRICAO = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.DESCRICAO;
    private static final String T_TABELA = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TABELA;
    private static final String T_EMAIL = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.EMAIL;
    private static final String T_ENDERECO = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.ENDERECO;
    private static final String T_LOGIN = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.LOGIN;
    private static final String T_SENHA = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.SENHA;
    private static final String T_TELEFONE = com.godofburguer.app.godofburguer.dao.tabelas.Usuarios.TELEFONE;

    SincronizaBancoWs ws;
    Dml crud;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_usuarios);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        ws = new SincronizaBancoWs(ListagemUsuariosActivity.this);
        crud = new Dml(ListagemUsuariosActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewUsuarios);
        bttAddUsuario = (FloatingActionButton)findViewById(R.id.bttAddUsuario);
        mSheetLayout = (SheetLayout)findViewById(R.id.bottom_sheet_usuarios);
        mSheetLayout.setFab(bttAddUsuario);
        mSheetLayout.setFabAnimationEndListener(this);
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, CadastroUsuarioActivity.class);
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


    public void botoes(){

        bttAddUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
;        });

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

    public void atualizar() {

        ws.atualizarUsuarios();

        //Faz o select de todos os dados passando por parametros, a tabela, os campos e a ordem
        String[] campos =  {T_ID, T_DESCRICAO, T_EMAIL, T_ENDERECO, T_LOGIN, T_SENHA, T_TELEFONE};
        Cursor cursor = crud.getAll(T_TABELA, campos, T_ID+" ASC");

        ArrayList<Usuarios> list = new ArrayList<Usuarios>();

        if(cursor != null) {
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()) {
                    list.add(new Usuarios(
                            cursor.getString(cursor.getColumnIndexOrThrow(T_DESCRICAO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_ENDERECO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_TELEFONE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_LOGIN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_SENHA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(T_ID))));

                    cursor.moveToNext();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Nenhum Lançamento encontrado!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(ListagemUsuariosActivity.this));
        recyclerView.setAdapter(new NotesAdapter(ListagemUsuariosActivity.this,list));

    }

    public interface CallBack<T>{
        public void call();
    }

    public void excluir(){

        excluir(new ListagemUsuariosActivity.CallBack() {
            @Override
            public void call() {
            }
        });
    }

    public void excluir(final ListagemUsuariosActivity.CallBack callback) {

        if(excluirUsuario != null && !excluirUsuario.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuariosController controler = retrofit.create(UsuariosController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("id", excluirUsuario);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemUsuariosActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemUsuariosActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirUsuario ="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemUsuariosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

        private List<Usuarios> mNotes;
        private Context mContext;
        AlertDialog alerta;


        public NotesAdapter(Context context, List<Usuarios> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_usuarios, parent, false);

            NotesAdapter.ViewHolder viewHolder = new NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(NotesAdapter.ViewHolder viewHolder, final int position) {

            final Usuarios notes = mNotes.get(position);

            TextView id = viewHolder.id;
            id.setText(notes.getId());

            TextView nome = viewHolder.nome;
            nome.setText(notes.getNome());

            TextView login = viewHolder.login;
            login.setText(notes.getLogin());


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


        public void alerta(final Usuarios u){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListagemUsuariosActivity.this);

            builder.setTitle("Cadastro de Usuários");
            builder.setMessage("Escolha uma opção:");

            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent it = new Intent(ListagemUsuariosActivity.this, CadastroUsuarioActivity.class);
                    it.putExtra("usuario", u);
                    startActivity(it);
                    finish();
                }
            });

            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    excluirUsuario = u.getId();
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
            TextView id, nome, login;
            CardView card;

            public ViewHolder(View itemView) {
                super(itemView);

                id = (TextView)itemView.findViewById(R.id.cardIdUsuario);
                nome = (TextView)itemView.findViewById(R.id.cardNomeUsuario);
                login= (TextView)itemView.findViewById(R.id.cardLoginUsuario);
                card = (CardView)itemView.findViewById(R.id.cardUsuarios);
            }
        }
    }

}
