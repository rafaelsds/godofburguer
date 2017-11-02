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
import com.godofburguer.app.godofburguer.controller.FornecedoresController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Fornecedores;
import com.godofburguer.app.godofburguer.entidades.Insumos;
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

public class ListagemFornecedoresActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    private RecyclerView recyclerView;
    private FloatingActionButton bttAddFornecedor;
    private SheetLayout mSheetLayout;
    private String excluirFornecedor;

    private static final int REQUEST_CODE = 1;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_fornecedores);

        inicialise();
        botoes();

        atualizar();
    }


    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewFornecedores);
        bttAddFornecedor = (FloatingActionButton)findViewById(R.id.bttAddFornecedor);
        mSheetLayout = (SheetLayout)findViewById(R.id.bottom_sheet_fornecedores);
        mSheetLayout.setFab(bttAddFornecedor);
        mSheetLayout.setFabAnimationEndListener(this);
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, FornecedoresActivity.class);
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

        bttAddFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });
        
    }

    public void atualizar() {
        obter(new ListagemFornecedoresActivity.CallBack<List<Fornecedores>>(){
            @Override
            public void call(List<Fornecedores> objeto) {
                List<Fornecedores> list = new ArrayList<Fornecedores>();

                for(Fornecedores r : objeto){
                    list.add(new Fornecedores(r.getNome(),r.getEndereco(), r.getTelefone(),r.getEmail(),r.getId()));
                }

                if(list == null || list.isEmpty()){
                    Toast.makeText(ListagemFornecedoresActivity.this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(ListagemFornecedoresActivity.this));
                recyclerView.setAdapter(new ListagemFornecedoresActivity.NotesAdapter(ListagemFornecedoresActivity.this,list));


            }

            @Override
            public void call(){
            };

        });

    }

    public void obter(final ListagemFornecedoresActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornecedoresController controler = retrofit.create(FornecedoresController.class);

        Call<List<Fornecedores>> request = controler.list();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ListagemFornecedoresActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Buscando....");

        progressDoalog.show();

        request.enqueue(new Callback<List<Fornecedores>>() {
            @Override
            public void onResponse(Call<List<Fornecedores>> call, Response<List<Fornecedores>> response) {
                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(ListagemFornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    callback.call(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Fornecedores>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ListagemFornecedoresActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallBack<T>{
        public void call();
        public void call(T callList);
    }


    public void excluir(){

        excluir(new ListagemFornecedoresActivity.CallBack() {
            @Override
            public void call() {
            }

            @Override
            public void call(Object callList) {

            }
        });
    }

    public void excluir(final ListagemFornecedoresActivity.CallBack callback) {

        if(excluirFornecedor != null && !excluirFornecedor.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FornecedoresController controler = retrofit.create(FornecedoresController.class);

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("id", excluirFornecedor);

            Call<Boolean> request = controler.excluir(param);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(ListagemFornecedoresActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Excluindo....");
            progressDoalog.show();

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(ListagemFornecedoresActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        excluirFornecedor="";
                        atualizar();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(ListagemFornecedoresActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public class NotesAdapter extends RecyclerView.Adapter<ListagemFornecedoresActivity.NotesAdapter.ViewHolder> {

        private List<Fornecedores> mNotes;
        private Context mContext;
        AlertDialog alerta;


        public NotesAdapter(Context context, List<Fornecedores> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public ListagemFornecedoresActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_fornecedores, parent, false);

            ListagemFornecedoresActivity.NotesAdapter.ViewHolder viewHolder = new ListagemFornecedoresActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(ListagemFornecedoresActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final Fornecedores notes = mNotes.get(position);

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


        public void alerta(final Fornecedores u){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListagemFornecedoresActivity.this);

            builder.setTitle("Cadastro de Fornecedores");
            builder.setMessage("Escolha uma opção:");

            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent it = new Intent(ListagemFornecedoresActivity.this, FornecedoresActivity.class);
                    it.putExtra("fornecedor", u);
                    startActivity(it);
                    finish();
                }
            });

            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    excluirFornecedor = u.getId();
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

                id = (TextView)itemView.findViewById(R.id.cardIdFornecedor);
                nome = (TextView)itemView.findViewById(R.id.cardNomeFornecedor);
                telefone= (TextView)itemView.findViewById(R.id.cardTelefoneFornecedor);
                card = (CardView)itemView.findViewById(R.id.cardFornecedores);
            }
        }
    }

}
