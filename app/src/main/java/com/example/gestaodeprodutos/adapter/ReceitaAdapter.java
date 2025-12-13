package com.example.gestaodeprodutos.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.ReceitaModel;

import java.util.List;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ViewHolder> {

    private List<ReceitaModel> listaDados;

    public ReceitaAdapter(List<ReceitaModel> lista) {
        this.listaDados = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ele infla o layout de cada linha (item_transacao.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceitaModel item = listaDados.get(position);

        holder.textDescricao.setText(item.getDescricao());
        holder.textData.setText(item.getData());

        // Receita SEMPRE é verde
        holder.textValor.setText("+ R$ " + String.format("%.2f", item.getValor()));
        holder.textValor.setTextColor(Color.parseColor("#00E676"));
    }


    @Override
    public int getItemCount() {
        return listaDados.size();
    }

    // Classe interna que segura os componentes da tela (View Holder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescricao, textData, textValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.text_descricao);
            textData = itemView.findViewById(R.id.text_data);
            textValor = itemView.findViewById(R.id.text_valor);
        }
    }
}