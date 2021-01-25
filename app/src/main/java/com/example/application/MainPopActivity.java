package com.example.application;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainPopActivity extends Activity {

    private ImageView imageView;
    private Button closebtn;
    private String REFIMG_URL="http://54.180.140.180:3000/api/IOT/getRefrigeratorImage?user_idx=";

    String images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pop);

        imageView = findViewById(R.id.pop_refri_image);
        closebtn = findViewById(R.id.pop_refri_close);

        new GetImg().execute();



        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private Bitmap getImageFromURL(String imageurl){

        Bitmap bitmap = null;

        try {
            URL url = new URL(imageurl);
            URLConnection connection = url.openConnection();
            connection.connect();

            int Size = connection.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream(), Size);

            bitmap = BitmapFactory.decodeStream(bis);

            bis.close();


        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private class GetImg extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler hh = new HttpHandler();
            String jsonRES = hh.makeServiceCall(REFIMG_URL+1);  //user_idx 추가해야함


            if (jsonRES!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonRES);
                    JSONArray jsonArray = jsonObject.getJSONArray("res_Data");
                    Log.d("refriJSON : ", jsonObject.toString());
                    JSONObject c = jsonArray.getJSONObject(0);

                    images = c.getString("images");
                    Log.d("image:",images);




                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Glide.with(MainPopActivity.this)
                    .load(images).fitCenter().into(imageView);

        }
    }
}