package com.example.gestaodeprodutos.repository;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.gestaodeprodutos.model.Produto;
import com.example.gestaodeprodutos.network.ApiService;
import com.example.gestaodeprodutos.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository responsável por comunicar com a API (Retrofit) / Supabase.
 * --> A ViewModel chama métodos daqui.
 * --> Aqui você configura as keys e chama os endpoints do ApiService.
 */
public class DadosRepository {
    // ============================
    // ======= CONFIGURAR =========
    // ============================
    // 1) Coloque aqui sua API KEY (idealmente vir de BuildConfig ou outra forma segura).
    //    NÃO deixe chaves sensíveis hardcoded em produção.
    private final String API_KEY = "SUA_API_KEY_AQUI";                // <-- MUDAR
    private final String AUTH = "Bearer SUA_API_KEY_AQUI";           // <-- MUDAR (se for usar "Bearer ")

    // ============================
    // ======= PADRÃO / FIXO ======
    // ============================
    private ApiService apiService;

    public ProdutoRepository() {
        // Cria a implementação do ApiService via Retrofit (padrão)
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    /**
     * Lista produtos (GET /produtos)
     * Retorna um MutableLiveData com a lista (ou null em caso de erro).
     */
    public MutableLiveData<List<Produto>> listarProdutos() {
        MutableLiveData<List<Produto>> produtosLiveData = new MutableLiveData<>();

        // Chama o endpoint definido em ApiService
        apiService.listarProdutos(API_KEY, AUTH).enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                // Log útil para debug (mostra código e corpo)
                Log.d("API_DEBUG", "Codigo: " + response.code());
                Log.d("API_DEBUG", "Body: " + new Gson().toJson(response.body()));
                Log.d("API_DEBUG", "Erro: " + response.errorBody());

                if (response.isSuccessful()) {
                    // Se der certo, coloca a lista no LiveData
                    produtosLiveData.setValue(response.body());
                } else {
                    // Se der erro, coloca null (você pode opcionalmente colocar lista vazia)
                    produtosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                // Em caso de falha de rede/exceção, retornar null e logar o erro
                Log.e("API_DEBUG", "Falha ao listar produtos", t);
                produtosLiveData.setValue(null);
            }
        });

        return produtosLiveData;
    }

    /**
     * Insere um produto (POST /produtos)
     * Retorna MutableLiveData<Boolean> indicando sucesso (true) ou falha (false).
     */
    public MutableLiveData<Boolean> inserirProduto(Produto produto) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        // Chama o endpoint de inserir definido em ApiService
        apiService.inserirProduto(API_KEY, AUTH, produto).enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                // response.isSuccessful() => código 2xx
                sucesso.setValue(response.isSuccessful());
                Log.d("API_DEBUG", "Inserir produto - codigo: " + response.code());
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                // Em caso de falha de rede/exceção, marca como false
                Log.e("API_DEBUG", "Falha ao inserir produto", t);
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }
}
