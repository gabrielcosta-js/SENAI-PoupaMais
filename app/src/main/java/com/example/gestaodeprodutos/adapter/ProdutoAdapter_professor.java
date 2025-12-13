package com.example.gestaodeprodutos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaodeprodutos.R;
import com.example.gestaodeprodutos.model.Produto_professor;

import java.util.List;

public class ProdutoAdapter_professor extends RecyclerView.Adapter<ProdutoAdapter_professor.ViewHolder> {

    private List<Produto_professor> lista;
    private ProdutoListener listener;

    public interface ProdutoListener {
        void onEditarClick(Produto_professor p);
        void onDeletarClick(Produto_professor p);
    }

    public ProdutoAdapter_professor(List<Produto_professor> lista, ProdutoListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void atualizarLista(List<Produto_professor> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_despesa, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto_professor p = lista.get(position);
        holder.txtNome.setText(p.getNome());
        holder.txtPreco.setText("R$ " + p.getPreco());

        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(p));
        holder.btnExcluir.setOnClickListener(v -> listener.onDeletarClick(p));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome, txtPreco;
        Button btnEditar, btnExcluir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_titulo);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            btnEditar = itemView.findViewById(R.id.btn_anexar);
            btnExcluir = itemView.findViewById(R.id.btn_salvar);
        }
    }
}
