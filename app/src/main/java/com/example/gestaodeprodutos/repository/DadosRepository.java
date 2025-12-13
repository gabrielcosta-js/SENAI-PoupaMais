package com.example.gestaodeprodutos.repository;
import android.content.Context;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.ReceitaModel;
import com.example.gestaodeprodutos.viewmodel.DadosViewModel;
import com.example.gestaodeprodutos.network.SupabaseService;
import com.example.gestaodeprodutos.network.RetrofitSupabase;
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

    private SupabaseService apiService;

    private final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW";

    public DadosRepository(Context context) {
        apiService = RetrofitSupabase
                .getRetrofitInstance(context)
                .create(SupabaseService.class);
    }


    // =======================================================
    // 🔵 LISTAR PRODUTOS
    // Chama o GET da API e devolve uma lista de Despesas
    // =======================================================
    public void listarDespesa(MutableLiveData<List<DespesaModel>> produtosLiveData, String token) {

        // Chama o endpoint definido no ApiService
        apiService.listarDespesa(API_KEY, token).enqueue(new Callback<List<DespesaModel>>() {

            @Override
            public void onResponse(Call<List<DespesaModel>> call, Response<List<DespesaModel>> response) {

                // Log de depuração
                Log.d("API_DEBUG", "Codigo: " + response.code());
                Log.d("API_DEBUG", "Body: " + new Gson().toJson(response.body()));

                // Se deu certo → manda os dados para o LiveData
                if (response.isSuccessful()) {
                    produtosLiveData.setValue(response.body());
                } else {
                    produtosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<DespesaModel>> call, Throwable t) {
                produtosLiveData.setValue(null);
            }
        });
    }

    // =======================================================
    // 🔵 LISTAR PRODUTOS
    // Chama o GET da API e devolve uma lista de Receitas
    // =======================================================
    public void listarReceita(MutableLiveData<List<ReceitaModel>> produtosLiveData, String token) {

        // Chama o endpoint definido no ApiService
        apiService.listarReceita(API_KEY, token).enqueue(new Callback<List<ReceitaModel>>() {

            @Override
            public void onResponse(Call<List<ReceitaModel>> call, Response<List<ReceitaModel>> response) {

                // Log de depuração
                Log.d("API_DEBUG", "Codigo: " + response.code());
                Log.d("API_DEBUG", "Body: " + new Gson().toJson(response.body()));

                // Se deu certo → manda os dados para o LiveData
                if (response.isSuccessful()) {
                    produtosLiveData.setValue(response.body());
                } else {
                    produtosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ReceitaModel>> call, Throwable t) {
                produtosLiveData.setValue(null);
            }
        });
    }

    // =======================================================
    // 🔵 INSERIR Despesa (POST)
    // Envia a Despesa para o Supabase
    // =======================================================
    public MutableLiveData<Boolean> inserirDespesa(DespesaModel despesaModel, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.inserirDespesa(API_KEY, token, despesaModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                // Retorna true se o Supabase respondeu 200
                sucesso.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }

    // =======================================================
    // 🔵 INSERIR Receita (POST)
    // Envia Receita para o Supabase
    // =======================================================
    public MutableLiveData<Boolean> inserirReceita(ReceitaModel receitaModel, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.inserirReceita(API_KEY, token, receitaModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                // Retorna true se o Supabase respondeu 200
                sucesso.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sucesso.setValue(false);
            }
        });

        return sucesso;
    }

    // =======================================================
    // 🔵 ATUALIZAR Despesa (PATCH)
    // Usa filtro "eq.<id>" para escolher o registro correto
    // =======================================================
    public MutableLiveData<Boolean> alterarDespesa(DespesaModel despesaModel, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        // Supabase exige filtro: id=eq.123
        String id = "eq." + despesaModel.getId().toString();

        apiService.alterarDespesa(API_KEY, token, id, despesaModel).enqueue(new Callback<Void>() {
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

    // =======================================================
    // 🔵 ATUALIZAR Receita (PATCH)
    // Usa filtro "eq.<id>" para escolher o registro correto
    // =======================================================
    public MutableLiveData<Boolean> alterarReceita(ReceitaModel receitaModel, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        // Supabase exige filtro: id=eq.123
        String id = "eq." + receitaModel.getId().toString();

        apiService.alterarReceita(API_KEY, token, id, receitaModel).enqueue(new Callback<Void>() {
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

    // =======================================================
    // 🔵 DELETAR Despesa (DELETE)
    // Também usa filtro: id=eq.<id>
    // =======================================================
    public MutableLiveData<Boolean> deletarDespesa(int id, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.deletarDespesa(API_KEY, token, "eq." + id).enqueue(new Callback<Void>() {

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

    // =======================================================
    // 🔵 DELETAR Receita (DELETE)
    // Também usa filtro: id=eq.<id>
    // =======================================================
    public MutableLiveData<Boolean> deletarReceita(int id, String token) {

        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        apiService.deletarReceita(API_KEY, token, "eq." + id).enqueue(new Callback<Void>() {

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
