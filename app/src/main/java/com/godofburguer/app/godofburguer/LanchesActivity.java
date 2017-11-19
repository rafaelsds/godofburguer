package com.godofburguer.app.godofburguer;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.godofburguer.app.godofburguer.controller.LancheInsumoController;
import com.godofburguer.app.godofburguer.controller.LanchesController;
import com.godofburguer.app.godofburguer.controller.RootController;
import com.godofburguer.app.godofburguer.db.SincronizaBancoWs;
import com.godofburguer.app.godofburguer.entidades.Insumos;
import com.godofburguer.app.godofburguer.entidades.LancheInsumo;
import com.godofburguer.app.godofburguer.entidades.Lanches;
import com.godofburguer.app.godofburguer.entidades.TipoLanche;

import java.text.Normalizer;
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
    private Spinner spnTipoLanche;
    private Intent intent;


    private AutoCompleteTextView atcInsumo;
    private List<Insumos> listInsumos, listInsumosFiltro;

    private String idLanche;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanches);

        inicialise();
        botoes();

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
        spnTipoLanche = (Spinner)findViewById(R.id.spnTipoLanche);
        listInsumos = new ArrayList<>();

        intent = getIntent();

        SincronizaBancoWs.atualizarInsumos(new SincronizaBancoWs.CallBack<List<Insumos>>(){
            @Override
            public void call(List<Insumos> objeto){
                listInsumosFiltro = objeto;
                List<String> list = new ArrayList<String>();

                if(listInsumosFiltro.size()>0) {
                    for (Insumos f : listInsumosFiltro) {
                        list.add(f.getNome());
                    }
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(LanchesActivity.this, android.R.layout.simple_dropdown_item_1line, list);
                atcInsumo.setAdapter(adp);

            }
        }, LanchesActivity.this);

        SincronizaBancoWs.atualizarTipoLanche(new SincronizaBancoWs.CallBack<List<TipoLanche>>(){
            @Override
            public void call(List<TipoLanche> objeto){

                List<String> listTipoLanche = new ArrayList<String>();

                listTipoLanche.add("");

                for(TipoLanche t : objeto){
                    listTipoLanche.add(t.getNome());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(LanchesActivity.this,
                        android.R.layout.simple_spinner_item, listTipoLanche);
                spnTipoLanche.setAdapter(adapter);

                if(intent.getSerializableExtra("lanche") != null){
                    Lanches lanches = (Lanches) intent.getSerializableExtra("lanche");
                    preencherInformacoes(lanches);
                }else{
                    idLanche=null;
                }

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

                if(validaDados(edtDescricao.getText().toString(),edtValor.getText().toString(), spnTipoLanche.getSelectedItem().toString())){
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



        btnAddInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validaInsumo(atcInsumo.getText().toString())){
                    Boolean existe=false;
                    for(Insumos i : listInsumosFiltro){
                        if(i.getNome().toUpperCase().equals(atcInsumo.getText().toString().toUpperCase())){

                            existe=true;

                            for(Insumos q : listInsumos){
                                if(q.getId().equals(i.getId())){

                                    new SweetAlertDialog(LanchesActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("O insumo já está inserido!")
                                            .show();

                                    atcInsumo.setText("");
                                    atcInsumo.requestFocus();

                                    return;
                                }
                            }

                            listInsumos.add(new Insumos("",i.getNome(),i.getId()));
                        }
                    }

                    atcInsumo.setText("");
                    atcInsumo.requestFocus();

                    if(!existe)
                        new SweetAlertDialog(LanchesActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Insumo inválido!")
                                .show();

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(LanchesActivity.this));
                recyclerView.setAdapter(new LanchesActivity.NotesAdapter(LanchesActivity.this,listInsumos));

            }
        });

    }

    public interface CallBack{
        void call();
    }

    public boolean validaDados(String descricao, String valor, String tipoLanche) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            edtDescricao.setError(getString(R.string.informe_descricao));
            isValid = false;
        }

        if(valor.isEmpty()) {
            edtValor.setError(getString(R.string.informe_valor));
            isValid = false;
        }

        if(tipoLanche.isEmpty()) {
            ((TextView)spnTipoLanche.getSelectedView()).setError(getString(R.string.informe_tipo));
            isValid = false;
        }

        return isValid;
    }

    public boolean validaInsumo(String descricao) {
        Boolean isValid = true;

        if(descricao.isEmpty()) {
            atcInsumo.setError(getString(R.string.informe_descricao));
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


    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }


    public void inserir(final LanchesActivity.CallBack callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LanchesController controler = retrofit.create(LanchesController.class);

        HashMap<String, String> param = obterHashLanche();

        final SweetAlertDialog progressDoalog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDoalog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDoalog.setTitleText("Carregando...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        if(idLanche != null && !idLanche.isEmpty()){

            param.put("id",idLanche);

            Call<Boolean> request = controler.alterar(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDoalog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                    } else {

                        excluirInsumo(new CallBack() {
                            @Override
                            public void call() {
                            }
                        },idLanche);

                        for(Insumos l : listInsumos){

                            inserirInsumos(new LanchesActivity.CallBack(){
                                @Override
                                public void call(){
                                }
                            }, obterHashInsumo(l));

                        }

                        idLanche=null;

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

                        for(Insumos l : listInsumos){

                            inserirInsumos(new LanchesActivity.CallBack(){
                                @Override
                                public void call(){
                                }
                            }, obterHashInsumo(l));

                        }

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


    public void inserirInsumos(final LanchesActivity.CallBack callback, HashMap hashMapInsumos) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RootController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LancheInsumoController controler = retrofit.create(LancheInsumoController.class);

        Call<Boolean> request = controler.inserir(hashMapInsumos);

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LanchesActivity.this, "Erro: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LanchesActivity.this, "Erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void excluirInsumo(final CallBack callback, String idLancheP) {
        if(idLancheP != null && !idLancheP.isEmpty()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RootController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LancheInsumoController controler = retrofit.create(LancheInsumoController.class);

            HashMap<String, String> param = new HashMap<>();

            param.put("idLanche", idLancheP);

            Call<Boolean> request = controler.excluir_insumos(param);

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(LanchesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(LanchesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public HashMap<String, String> obterHashLanche(){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("nome", edtDescricao.getText().toString());
        hashMap.put("valor", edtValor.getText().toString());
        hashMap.put("tipoLanche", spnTipoLanche.getSelectedItem().toString());
        return hashMap;

    }

    public HashMap<String, String> obterHashInsumo(Insumos i){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("idInsumo", i.getId());
        hashMap.put("nome", i.getNome());
        hashMap.put("idLanche", idLanche);

        return hashMap;

    }


    public void preencherInformacoes(Lanches u){

        SincronizaBancoWs.atualizarLancheInsumo(new SincronizaBancoWs.CallBack<List<LancheInsumo>>(){
            @Override
            public void call(List<LancheInsumo> objeto){

                for(LancheInsumo i : objeto){
                    listInsumos.add(new Insumos("",i.getNome(),i.getIdInsumo()));
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(LanchesActivity.this));
                recyclerView.setAdapter(new LanchesActivity.NotesAdapter(LanchesActivity.this,listInsumos));

            }
        }, LanchesActivity.this, u);

        if(u.getId() != null)
            idLanche = u.getId();

        if(u.getNome() != null)
            edtDescricao.setText(u.getNome());

        if(!String.valueOf(u.getValor()).isEmpty())
            edtValor.setText(String.valueOf(u.getValor()));

        if(u.getTipoLanche() != null){
            for(int i= 0; i < spnTipoLanche.getCount(); i++)
            {
                if(spnTipoLanche.getItemAtPosition(i).toString().toUpperCase().equals(u.getTipoLanche().toUpperCase()))
                {
                    spnTipoLanche.setSelection(i);
                    return;
                }
            }
        }

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


        private void alerta(final Insumos u){

            new SweetAlertDialog(LanchesActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Deseja excluir o insumo?")
                    .setContentText("O insumo será excluido!")
                    .setCancelText("Não")
                    .setConfirmText("Sim")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            listInsumos.remove(u);
                            recyclerView.setLayoutManager(new LinearLayoutManager(LanchesActivity.this));
                            recyclerView.setAdapter(new LanchesActivity.NotesAdapter(LanchesActivity.this,listInsumos));
                            sweetAlertDialog.cancel();
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

                id = (TextView)itemView.findViewById(R.id.cardIdInsumo);
                nome = (TextView)itemView.findViewById(R.id.cardDescricaoInsumo);
                card = (CardView)itemView.findViewById(R.id.cardInsumos);
            }
        }
    }

}
