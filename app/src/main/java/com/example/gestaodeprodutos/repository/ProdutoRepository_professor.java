package com.example.gestaodeprodutos.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.gestaodeprodutos.model.Produto_professor;
import com.example.gestaodeprodutos.network.ApiService_professor;
import com.example.gestaodeprodutos.network.RetrofitClient_professor;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// O Repository é responsável por acessar a API.
// Ele separa a ViewModel da lógica de rede.
public class ProdutoRepository_professor {

    private ApiService_professor apiService;

    // API KEY e Bearer Key
    private final String API_KEY = "sb_publishable_eN4SSA3iUkrz-EgXNdJqpQ_Fdh8Dn20";

    public ProdutoRepository_professor() {
        apiService = RetrofitClient_professor.getRetrofitInstance().create(ApiService_professor.class);
    }

    public void listarProdutos(MutableLiveData<List<Produto_professor>> produtosLiveData, String token) {

        apiService.listarProdutos(API_KEY, token).enqueue(new Callback<List<Produto_professor>>() {
            @Override
            public void onResponse(Call<List<Produto_professor>> call, Response<List<Produto_professor>> response) {

                Log.d("API_DEBUG", "Codigo: " + response.code());
                Log.d("API_DEBUG", "Body: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    produtosLiveData.setValue(response.body());
                } else {
                    produtosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Produto_professor>> call, Throwable t) {
                produtosLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<Boolean> inserirProduto(Produto_professor produto, String token) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.inserirProduto(API_KEY, token, produto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sucesso.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }

    public MutableLiveData<Boolean> atualizarProduto(Produto_professor produto, String token) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();
        String id = "eq." + produto.getId().toString();

        apiService.atualizarProduto(API_KEY, token, id, produto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sucesso.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }

    public MutableLiveData<Boolean> deletarProduto(int id, String token) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.deletarProduto(API_KEY, token, "eq." + id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sucesso.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }
}