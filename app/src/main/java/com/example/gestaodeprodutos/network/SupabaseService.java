package com.example.gestaodeprodutos.network;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.ReceitaModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SupabaseService {
    @GET("/rest/v1/despesas")
    Call<List<DespesaModel>> getDespesas(@Query("select") String select);

    @GET("/rest/v1/receitas")
    Call<List<ReceitaModel>> getReceitas(@Query("select") String select);
}