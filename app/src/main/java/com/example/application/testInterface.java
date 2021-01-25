package com.example.application;

import org.json.JSONArray;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface testInterface {

    @Multipart
    @POST("/api/test2/test")
    Call<RecipeaddItem> example ( @Part Map<String, RequestBody> json,
            //List<Integer>
            @Part("multipartChk") JSONArray jsonArray,
            @Part MultipartBody.Part titleImage);
}
