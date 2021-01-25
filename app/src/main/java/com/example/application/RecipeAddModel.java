package com.example.application;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RecipeAddModel {

    public static class RQ{
        private MultipartBody.Part titleImage;
        private MultipartBody.Part descriptionImage;
        private HashMap<String, RequestBody> rqMap;

        public RQ(final String path_title,final String path_desc,final JsonObject jsonObject){
            super();
            rqMap = new HashMap<>();
            File file_title, file_description;

            if (path_title !=null){
                file_title = new File(path_title);
                RequestBody rqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_title);
                titleImage = MultipartBody.Part.createFormData("titleImage",file_title.getName(),rqFile);
            }

            if (path_desc !=null){
                file_description = new File(path_desc);
                RequestBody rqFile = RequestBody.create(MediaType.parse("multipart/form-data"),file_description);
                descriptionImage = MultipartBody.Part.createFormData("descriptionImage",file_description.getName(),rqFile);
            }

            RequestBody rqJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),String.valueOf(jsonObject));

            rqMap.put("recipe",rqJson);
        }

        public MultipartBody.Part getTitleImage(){
            return titleImage;
        }
        public MultipartBody.Part getDescriptionImage(){
            return descriptionImage;
        }

    }

    public static class RS extends BaseResponseModel{
        @Expose
        public Response response;

        public static class Response{
            @Expose
            private String res_State;
            @Expose
            private String res_Msg;
        }
    }



}
