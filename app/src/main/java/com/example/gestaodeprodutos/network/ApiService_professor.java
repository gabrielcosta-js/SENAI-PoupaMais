package com.example.gestaodeprodutos.network;

import com.example.gestaodeprodutos.model.Produto_professor;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService_professor {

    @Headers({
            "Accept: application/json",
            "Prefer: return=representation"
    })
    @GET("rest/v1/produtos?select=*&order=nome.asc")
    Call<List<Produto_professor>> listarProdutos(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth
    );

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/produtos")
    Call<Void> inserirProduto(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Body Produto_professor produto
    );

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/produtos")
    Call<Void> atualizarProduto(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Query("id") String id,
            @Body Produto_professor produto
    );

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @DELETE("rest/v1/produtos")
    Call<Void> deletarProduto(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Query("id") String id
    );
}
