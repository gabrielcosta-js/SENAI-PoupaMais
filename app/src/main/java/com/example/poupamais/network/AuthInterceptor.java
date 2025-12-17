package com.example.poupamais.network;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = context
                .getSharedPreferences("APP", Context.MODE_PRIVATE)
                .getString("TOKEN", null);

        Request request = chain.request();

        if (token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", token)
                    .build();
        }

        return chain.proceed(request);
    }
}
