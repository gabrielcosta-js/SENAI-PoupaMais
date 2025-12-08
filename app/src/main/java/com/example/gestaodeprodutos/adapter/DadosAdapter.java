package com.example.gestaodeprodutos.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.Dados;

import java.util.List;

public class DadosAdapter extends RecyclerView.Adapter<DadosAdapter.ViewHolder> {

    private List<Dados> listaDados;

    public DadosAdapter(List<Dados> lista) {
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
        Dados item = listaDados.get(position);

        // Preenche os textos
        holder.textDescricao.setText(item.getDescricao());
        holder.textData.setText(item.getData());

        // Lógica das Cores (Verde para Receita, Vermelho para Despesa)
        if ("RECEITA".equalsIgnoreCase(item.getTipo())) {
            holder.textValor.setText("+ R$ " + String.format("%.2f", item.getValor()));
            holder.textValor.setTextColor(Color.parseColor("#00E676")); // Verde
        } else {
            holder.textValor.setText("- R$ " + String.format("%.2f", item.getValor()));
            holder.textValor.setTextColor(Color.parseColor("#FF5252")); // Vermelho
        }
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