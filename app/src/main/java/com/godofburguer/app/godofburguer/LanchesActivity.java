package com.godofburguer.app.godofburguer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.db.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.Lanches;

import java.util.ArrayList;
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

public class LanchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnCancelar,btnGravar, btnAddInsumo;
    private EditText edtDescricao, edtValor;
    private Intent intent;

    private AutoCompleteTextView atcInsumo;
    private List<Insumos>listInsumos;

    private String idLanche;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanches);

        inicialise();
        botoes();

        if(intent.getSerializableExtra("lanche") != null){
            Lanches lanches = (Lanches) intent.getSerializableExtra("lanche");
            preencherInformacoes(lanches);
        }else{
            idLanche=null;
        }


        List<Insumos> objeto = new ArrayList<>();
        objeto.add(new Insumos("Ambev","Pão","1"));
        objeto.add(new Insumos("Ambev","grãos","2"));
        objeto.add(new Insumos("Ambev","tomate","3"));
        objeto.add(new Insumos("Ambev","alface","4"));
        objeto.add(new Insumos("Ambev","presunto","5"));
        objeto.add(new Insumos("Ambev","queijo","6"));

        recyclerView.setLayoutManager(new LinearLayoutManager(LanchesActivity.this));
        recyclerView.setAdapter(new LanchesActivity.NotesAdapter(LanchesActivity.this,objeto));

    }

    public void inicialise(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCadLanche);
        btnGravar = (Button)findViewById(R.id.btnGravarLanche);
        btnAddInsumo = (Button)findViewById(R.id.btnAddInsumoLanche);
        edtDescricao = (EditText)findViewById(R.id.editNomeLanche);
        edtValor = (EditText)findViewById(R.id.editValorLanche);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewInsumosLanche);
        atcInsumo = (AutoCompleteTextView)findViewById(R.id.atcNomeInsumoLanche);
        intent = getIntent();

        SincronizaBancoWs.atualizarInsumos(new SincronizaBancoWs.CallBack<List<Insumos>>(){
            @Override
            public void call(List<Insumos> objeto){
                listInsumos = objeto;
            }
        }, LanchesActivity.this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListagemLanchesActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(getApplicationContext(), ListagemLanchesActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }


    public void botoes(){
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideSoftkeyBoard.hideSoftKeyboard(LanchesActivity.this);

                if(validaDados(edtDescricao.getText().toString(),edtValor.getText().toString())){
                    inserir();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                startActivity(it);
                finish();
            }
        });


        atcInsumo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(listInsumos.size() >0){
                        List<String>list = new ArrayList<String>();

                        for(Insumos f : listInsumos){
                            if(atcInsumo.getText() != null &&  f.getNome().toUpperCase().startsWith(atcInsumo.getText().toString().toUpperCase())){
                                list.add(f.getNome());
                            }
                        }

                        ArrayAdapter<String> adp = new ArrayAdapter<String>(LanchesActivity.this, android.R.layout.simple_dropdown_item_1line, list);
                        atcInsumo.setAdapter(adp);
                    }
                }
            }
        });


        btnAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public interface CallBack{
        void call();
    }

    public boolean validaDados(String descricao, String valor) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            edtDescricao.setError(getString(R.string.informe_descricao));
            isValid = false;
        }

        if(valor.isEmpty()) {
            edtValor.setError(getString(R.string.informe_valor));
            isValid = false;
        }

        return isValid;
    }

    public void inserir(){
        inserir(new LanchesActivity.CallBack(){
            @Override
            public void call() {
            }
        });
    }

    public void inserir(final LanchesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controler = retrofit.create(LanchesController.class);

        HashMap<String, String> param = obterHashUsuario();

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        if(idLanche != null){

            param.put("id",idLanche);
            idLanche=null;

            Call<Boolean> request = controler.alterar(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(LanchesActivity.this, "Erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Call<Boolean> request = controler.inserir(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        callback.call();
                        Intent it = new Intent(LanchesActivity.this, ListagemLanchesActivity.class);
                        startActivity(it);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(LanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public HashMap<String, String> obterHashUsuario(){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("valor", edtValor.getText().toString());

        return hashMap;

    }

    public void preencherInformacoes(Lanches u){

        if(u.getId() != null)
            idLanche = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

        if(!String.valueOf(u.getValor()).isEmpty())
            edtValor.setText(String.valueOf(u.getValor()));

    }


    private class NotesAdapter extends RecyclerView.Adapter<LanchesActivity.NotesAdapter.ViewHolder> {

        private List<Insumos> mNotes;
        private Context mContext;
        AlertDialog alerta;


        private NotesAdapter(Context context, List<Insumos> notes) {
            mNotes = notes;
            mContext = context;
        }


        @Override
        public LanchesActivity.NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View notesView = inflater.inflate(R.layout.card_insumos, parent, false);

            LanchesActivity.NotesAdapter.ViewHolder viewHolder = new LanchesActivity.NotesAdapter.ViewHolder(notesView);
            return viewHolder;
        }


        private Context getContext() {
            return mContext;
        }

        @Override
        public void onBindViewHolder(LanchesActivity.NotesAdapter.ViewHolder viewHolder, final int position) {

            final Insumos notes = mNotes.get(position);

            TextView id = viewHolder.id;
            id.setText(notes.getId());

            TextView nome = viewHolder.nome;
            nome.setText(notes.getNome());


//            viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Vibrar();
//                    alerta(notes);
//                    return false;
//                }
//            });

        }


        private void Vibrar(){
            Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long milliseconds = 50;
            rr.vibrate(milliseconds);
        }


//        private void alerta(final Insumos u){
//            AlertDialog.Builder builder = new AlertDialog.Builder(LanchesActivity.this);
//
//            builder.setTitle("Cadastro de Insumos");
//            builder.setMessage("Escolha uma opção:");
//
//            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    Intent it = new Intent(LanchesActivity.this, InsumosActivity.class);
//                    it.putExtra("insumo", u);
//                    startActivity(it);
//                    finish();
//                }
//            });
//
//            builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    insumoExcluir = u.getId();
//
//                    excluirInsumo(new CallBack() {
//                        @Override
//                        public void call() {
//                        }
//                    });
//
//                }
//            });
//
//            alerta = builder.create();
//            alerta.show();
//        }

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
