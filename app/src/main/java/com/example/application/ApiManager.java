package com.example.application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String URL="http://54.180.140.180";
    private static OkHttpClient.Builder httpClientBuilder;

    private static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor){
        httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.addInterceptor(interceptor);
        httpClientBuilder.connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(100,TimeUnit.SECONDS)
                .writeTimeout(100,TimeUnit.SECONDS)
                .build();
        return httpClientBuilder.build();
    }

    private static HttpLoggingInterceptor provideLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static Retrofit mBuilder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .build();

    public static <T> T createService(Class<T> serviceClass){
        return mBuilder.create(serviceClass);
    }
}

