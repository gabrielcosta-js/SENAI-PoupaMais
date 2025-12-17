package com.example.poupamais.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poupamais.R;
import com.example.poupamais.model.Dados;

import java.util.List;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.ViewHolder> {

    private List<Dados> lista;

    public MovimentacaoAdapter(List<Dados> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dados item = lista.get(position);

        holder.txtDescricao.setText(item.getDescricao());
        holder.txtData.setText(item.getData());

        if ("RECEITA".equals(item.getTipo())) {
            holder.txtValor.setText("+ R$ " + String.format("%.2f", item.getValor()));
            holder.txtValor.setTextColor(Color.parseColor("#00E676"));
        } else {
            holder.txtValor.setText("- R$ " + String.format("%.2f", item.getValor()));
            holder.txtValor.setTextColor(Color.parseColor("#FF5252"));
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtData, txtValor;

        ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.text_descricao);
            txtData = itemView.findViewById(R.id.text_data);
            txtValor = itemView.findViewById(R.id.text_valor);
        }
    }
}
