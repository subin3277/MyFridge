package com.example.application;

import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//api/recipe/postRecipe
//api/test2/upload
public interface APIsInterface {

    @Multipart
    @POST("/api/recipe/postRecipe")
    Call<RecipeaddItem> uploadImage(@Part("recipe") String recipe,
                                    @Part("ingredients") String ingredients,
                                    @Part("description") String description,
                                    @Part MultipartBody.Part titleImage,
                                    @Part MultipartBody.Part[] descriptionImage,
                                    @Part("descriptionImageChk") String descriptionImageChk
                                    );

}
