package com.example.poupamais.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poupamais.R;
import com.example.poupamais.model.ReceitaModel;

import java.util.List;
import java.util.Locale;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ViewHolder> {

    private List<ReceitaModel> listaDados;
    private OnReceitaClickListener listener;

    // 🔹 INTERFACE DE CLIQUE
    public interface OnReceitaClickListener {
        void onClick(ReceitaModel receita);
    }

    // 🔹 CONSTRUTOR COM LISTENER
    public ReceitaAdapter(List<ReceitaModel> lista, OnReceitaClickListener listener) {
        this.listaDados = lista;
        this.listener = listener;
    }

    public void atualizarLista(List<ReceitaModel> novaLista) {
        this.listaDados = novaLista;
        notifyDataSetChanged();
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
        ReceitaModel item = listaDados.get(position);

        holder.textDescricao.setText(item.getNome_receita());
        holder.textData.setText(item.getData());

        // Receita sempre positiva (verde)
        holder.textValor.setText(
                "+ R$ " + String.format(Locale.getDefault(), "%.2f", item.getValor())
        );
        holder.textValor.setTextColor(Color.parseColor("#00E676"));

        // Ícone por categoria
        defineIconePorCategoria(item.getCategoria(), holder.imgIconeReceita);

        // 🔥 CLIQUE NO ITEM
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDados != null ? listaDados.size() : 0;
    }

    private void defineIconePorCategoria(String categoria, ImageView imageView) {
        int drawableRes;

        switch (categoria.toLowerCase(Locale.ROOT)) {
            case "salário":
            case "salario":
                drawableRes = R.drawable.ic_work;
                break;
            case "freelance":
                drawableRes = R.drawable.ic_attach;
                break;
            case "investimento":
                drawableRes = R.drawable.ic_trending_up;
                break;
            case "presente":
                drawableRes = R.drawable.ic_card_giftcard;
                break;
            case "economia":
                drawableRes = R.drawable.ic_savings;
                break;
            default:
                drawableRes = R.drawable.ic_attach_money;
                break;
        }

        imageView.setImageResource(drawableRes);
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescricao, textData, textValor;
        ImageView imgIconeReceita;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.text_descricao);
            textData = itemView.findViewById(R.id.text_data);
            textValor = itemView.findViewById(R.id.text_valor);
            imgIconeReceita = itemView.findViewById(R.id.img_icone_receita);
        }
    }
}
