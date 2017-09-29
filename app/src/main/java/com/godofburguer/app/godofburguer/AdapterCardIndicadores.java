package com.godofburguer.app.godofburguer;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.godofburguer.app.godofburguer.entidades.Indicador;

import java.util.List;

public class AdapterCardIndicadores extends RecyclerView.Adapter<ViewIndicadores>{
    List<Indicador> list;

    public AdapterCardIndicadores(List<Indicador> list) {
        this.list = list;
    }

    @Override
    public ViewIndicadores onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_indicadores,viewGroup,false);
        return new ViewIndicadores(view);
    }

    @Override
    public void onBindViewHolder(ViewIndicadores myViewHolder, int position) {
        Indicador myObject = list.get(position);
        myViewHolder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
