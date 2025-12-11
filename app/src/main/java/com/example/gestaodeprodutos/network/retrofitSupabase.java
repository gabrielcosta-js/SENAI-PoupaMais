package com.example.gestaodeprodutos.network;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe responsável por criar a conexão com a API do Supabase

public class retrofitSupabase {
    private static Retrofit retrofit;

    // 🔵 ALTERAR AQUI:
    // URL do seu projeto Supabase (NÃO inclui /rest/v1)
    private static final String BASE_URL = "https://SEU-PROJETO.supabase.co";

    // 🔵 ALTERAR AQUI:
    // Coloque sua chave ANON — nunca use service_role!
    private static final String API_KEY = "SUA_API_KEY_AQUI";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // ✔ (Padrão) Mostrar logs no console — não mexer
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // ✔ (Padrão) Criação do cliente HTTP
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging) // log de requisições
                    .addInterceptor(new Interceptor() {

                        // ✔ (Padrão) Interceptor que adiciona headers em TODAS as requisições
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            // Requisição original
                            Request original = chain.request();

                            // Criação da nova requisição com cabeçalhos do Supabase
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("apikey", API_KEY) // 🔵 Envia a API KEY automaticamente
                                    .header("Authorization", "Bearer " + API_KEY) // 🔵 Token obrigatório
                                    .header("Content-Type", "application/json") // formato JSON
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            // ✔ (Padrão) Configuração final do Retrofit — não mexer
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 🔵 Apenas aqui você mexe
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
