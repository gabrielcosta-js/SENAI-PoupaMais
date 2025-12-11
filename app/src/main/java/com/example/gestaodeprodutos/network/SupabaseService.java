package com.example.gestaodeprodutos.network;

import com.example.gestaodeprodutos.model.DespesaModel;
import com.example.gestaodeprodutos.model.Produto_professor;
import com.example.gestaodeprodutos.model.ReceitaModel;
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

/**
 * ApiService_professor
 *
 * Aqui definimos TODAS as rotas que o Retrofit pode chamar.
 *
 * O QUE VOCÊ SEMPRE MUDA EM CADA PROJETO:
 *  ✔ Nome da tabela (produtos)
 *  ✔ Nome do model (Produto_professor)
 *  ✔ Campos do model
 *  ✔ Filtros do Supabase (select, order, where etc.)
 *  ✔ Tipo retornado (Void, Produto_professor etc.)
 *
 * O RESTO É PADRÃO E VOCÊ NÃO PRECISA ALTERAR.
 */

public interface SupabaseService {
//    @GET("/rest/v1/despesas")
//    Call<List<DespesaModel>> getDespesas(@Query("select") String select);
//
//    @GET("/rest/v1/receitas")
//    Call<List<ReceitaModel>> getReceitas(@Query("select") String select);


    // ================================
    // ========== LISTAR (GET) ========
    // ================================

    @Headers({
            "Accept: application/json",          // PADRÃO - não mexe
            "Prefer: return=representation"      // PADRÃO - não mexe
    })
    @GET("rest/v1/despesas?select=*")// Isso aqui serve pra eu escolher qual dado eu quero puxar da tabela
        //                   ^^^^^^^^  ^^^^^^^^^^^^^^^^^
        //          MEXE AQUI → nome da tabela do Supabase
        //          MEXE AQUI → filtros (select, order)
    Call<List<DespesaModel>> listarDespesa( // ListarDespesa
            @Header("apikey") String apiKey,          // NÃO MUDA
            @Header("Authorization") String auth      // NÃO MUDA
    );

    @GET("rest/v1/receitas?select=*")// Isso aqui serve pra eu escolher qual dado eu quero puxar da tabela
        //                   ^^^^^^^^  ^^^^^^^^^^^^^^^^^
        //          MEXE AQUI → nome da tabela do Supabase
        //          MEXE AQUI → filtros (select, order)
    Call<List<ReceitaModel>> listarReceita( //ListarReceita
            @Header("apikey") String apiKey,          // NÃO MUDA
            @Header("Authorization") String auth      // NÃO MUDA
    );







    // ================================
    // ========= INSERIR (POST) =======
    // ================================

    @Headers({
            "Content-Type: application/json",         // PADRÃO
            "Prefer: return=representation"           // PADRÃO
    })
    @POST("rest/v1/despesas")
        //           ^^^^^^^^
        //     MEXE AQUI → nome da tabela
    Call<Void> inserirDespesa(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Body DespesaModel despesaModel           // MEXE → tipo do objeto enviado
    );

    @POST("rest/v1/receitas")
        //           ^^^^^^^^
        //     MEXE AQUI → nome da tabela
    Call<Void> inserirReceita(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Body ReceitaModel receitaModel           // MEXE → tipo do objeto enviado
    );

    // ================================
    // ======== ATUALIZAR (PATCH) =====
    // ================================

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/despesas")
        //           ^^^^^^^^
        //     MEXE AQUI → nome da tabela
    Call<Void> alterarDespesa(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,

            // Query parameter vira: ?id=eq.ID
            @Query("id") String id,                 // MEXE → nome do campo usado no filtro (id)
            // Supabase transforma isso em: id=eq.valor

            @Body DespesaModel despesaModel         // MEXE → tipo do objeto
    );


    @PATCH("rest/v1/receitas")
        //           ^^^^^^^^
        //     MEXE AQUI → nome da tabela
    Call<Void> alterarReceita(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,

            // Query parameter vira: ?id=eq.ID
            @Query("id") String id,                 // MEXE → nome do campo usado no filtro (id)
            // Supabase transforma isso em: id=eq.valor

            @Body ReceitaModel receitaModel          // MEXE → tipo do objeto
    );



    // ================================
    // =========== DELETAR ============
    // ================================

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @DELETE("rest/v1/despesas")
        //             ^^^^^^^^
        //       MEXE AQUI → nome da tabela
    Call<Void> deletarDespesa(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Query("id") String id                  // MEXE → campo utilizado no WHERE
            // Fica assim: ?id=eq.3
    );
    @DELETE("rest/v1/receitas")
        //             ^^^^^^^^
        //       MEXE AQUI → nome da tabela
    Call<Void> deletarReceita(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Query("id") String id                  // MEXE → campo utilizado no WHERE
            // Fica assim: ?id=eq.3
    );
}