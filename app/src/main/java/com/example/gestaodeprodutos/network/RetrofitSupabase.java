package com.example.gestaodeprodutos.network;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSupabase {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://hiojmrdypibzlaxirbal.supabase.co/";
    private static final String API_KEY = "sb_secret_Eq6N9jRApVFcGFJ-HhbwXw_zJRaukhW";

    public static Retrofit getRetrofitInstance(Context context) {

        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            String token = context
                                    .getSharedPreferences("APP", Context.MODE_PRIVATE)
                                    .getString("TOKEN", "");

                            Request request = chain.request().newBuilder()
                                    .header("apikey", API_KEY)
                                    .header("Authorization", token) // TOKEN REAL DO USUÁRIO
                                    .header("Content-Type", "application/json")
                                    .build();

                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
