package com.example.gestaodeprodutos.network;


import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe responsável por criar uma instância única do Retrofit.
// Aqui configuramos o endpoint do Supabase.
// WHRitEEBUcPalFwY
public class RetrofitClient {

    private static Retrofit retrofit;

    // URL do Banco Supabase
    private static final String BASE_URL = "https://hiojmrdypibzlaxirbal.supabase.co";

    // Chave API Banco
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imhpb2ptcmR5cGliemxheGlyYmFsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQyNDI4MTcsImV4cCI6MjA3OTgxODgxN30.XciHZ3e8s2jyoF_ZbO-EyVEkvDBNUOMAPqV5tc29gT0";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // Configuração do Log (Para ver os dados no console do Android Studio)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configuração do Cliente HTTP (Injetando a Chave API em TODAS as chamadas)
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging) // Adiciona o log
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // insere os headers obrigatórios do Supabase
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("apikey", API_KEY)
                                    .header("Authorization", "Bearer " + API_KEY)
                                    .header("Content-Type", "application/json")
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            // criando a instância Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}