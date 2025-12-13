package com.example.gestaodeprodutos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.DespesaModel;

import java.util.List;
import java.util.Locale;

public class DespesaAdapter extends RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder> {

    private List<DespesaModel> listaDespesas;

    public DespesaAdapter(List<DespesaModel> listaDespesas) {
        this.listaDespesas = listaDespesas;
    }
    public void atualizarLista(List<DespesaModel> novaLista) {
        this.listaDespesas = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    // MÉTODO 1: Cria o ViewHolder (Infla o layout de cada item)
    public DespesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_despesa, parent, false);
        return new DespesaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DespesaViewHolder holder, int position) {
        DespesaModel despesa = listaDespesas.get(position);
        holder.txtNomeDespesa.setText(despesa.getDescricao());
        holder.txtCategoriaDespesa.setText(despesa.getCategoria());

        String valorFormatado = String.format(Locale.getDefault(), "R$ %.2f", Math.abs(despesa.getValor()));

        if (despesa.getValor() < 0) {
            holder.txtValorDespesa.setText("- " + valorFormatado);
            holder.txtValorDespesa.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.txtValorDespesa.setText("+ " + valorFormatado);
            holder.txtValorDespesa.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }

        defineIconePorCategoria(despesa.getCategoria(), holder.imgIconeDespesa);
    }

    @Override
    // MÉTODO 3: Retorna o número total de itens na lista
    public int getItemCount() {
        return listaDespesas.size();
    }
    private void defineIconePorCategoria(String categoria, ImageView imageView) {
        int drawableRes;

        switch (categoria.toLowerCase(Locale.ROOT)) {
            case "mercado":
            case "compras":
                drawableRes = R.drawable.ic_shopping_cart;
                break;
            case "transporte":
            case "combustivel":
                drawableRes = R.drawable.ic_directions_car;
                break;
            case "alimentação":
            case "restaurante":
                drawableRes = R.drawable.ic_restaurant;
                break;
            case "saúde":
            case "farmácia":
                drawableRes = R.drawable.ic_favorite;
                break;
            case "serviços":
            case "streaming":
            case "lazer":
                drawableRes = R.drawable.ic_smartphone;
                break;
            case "moradia":
                drawableRes = R.drawable.ic_home;
                break;

            default:
                drawableRes = R.drawable.ic_more_horiz;
                break;
        }
        imageView.setImageResource(drawableRes);
    }

    public static class DespesaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIconeDespesa;
        TextView txtNomeDespesa;
        TextView txtCategoriaDespesa;
        TextView txtValorDespesa;

        public DespesaViewHolder(View itemView) {
            super(itemView);
            imgIconeDespesa = itemView.findViewById(R.id.img_icone_despesa);
            txtNomeDespesa = itemView.findViewById(R.id.txt_nome_despesa);
            txtCategoriaDespesa = itemView.findViewById(R.id.txt_categoria_despesa);
            txtValorDespesa = itemView.findViewById(R.id.txt_valor_despesa);
        }
    }
}