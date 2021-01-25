package com.example.application;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExampleAddrecipeActivity extends AppCompatActivity {

    EditText text;
    ImageButton btn,btn1;
    Uri uri;
    Button send;
    String uri_title,stringtext;
    JSONObject jsonObject;
    //ArrayList multipartChk;
    JSONArray multipartChk;
    List<String> multipartChk2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_addrecipe);

        text=findViewById(R.id.test_text);
        btn=findViewById(R.id.test_image);
        btn1=findViewById(R.id.test_image1);
        send = findViewById(R.id.sendbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringtext = text.getText().toString();

                multipartChk = new JSONArray();

                multipartChk2 = new ArrayList<>();

/*

                if (btn != null){
                    multipartChk.put(1);
                    multipartChk2.add(0,1);
                }
                else {
                    multipartChk.put(1);
                }

                if (btn1!=null){
                    multipartChk.put(0);
                    multipartChk2.add(1,0);
                }
                else {
                    multipartChk.put(0);
                    multipartChk2.add(1,0);
                }
                Log.d("리스트", multipartChk.toString());
*/



                uploadresponse();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("사진업로드","1");

        if (resultCode==RESULT_OK&&data!=null) {

            uri=data.getData();

            btn.setImageURI(uri);
        }
        else {
            Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadresponse() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl("http://54.180.140.180:3000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String realpath=getRealPathFromURI(uri);
        Log.e("realpath",realpath);

        File titleImageFile = new File(realpath);
        RequestBody titleImage = RequestBody.create(MediaType.parse("image/*"), titleImageFile);
        MultipartBody.Part titleImagePart = MultipartBody.Part.createFormData("multipart", titleImageFile.getName(), titleImage);

        HashMap<String, RequestBody> map = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        jsonObject = new JSONObject();

        try {
            jsonObject.put("a",1);
            jsonObject.put("b",0);
        } catch (JSONException e){
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);

        String[][] multipart = new String[2][2];
        multipart[0][0]="1";
        multipart[0][1]="1번";
        multipart[1][0]= "2";
        multipart[1][1]="2번";


        map.put("json", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject)));

        HashMap<String, RequestBody> map2 = new HashMap<>();
        map2.put("multipartChk", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject)));



        final testInterface testInterface = client.create(testInterface.class);
        Call<RecipeaddItem> surveyResponse = null;
        surveyResponse = testInterface.example(map, multipartChk, titleImagePart);
        surveyResponse.enqueue(new Callback<RecipeaddItem>() {
            @Override
            public void onResponse(Call<RecipeaddItem> call, Response<RecipeaddItem> response) {

                if (response.isSuccessful()){
                    Toast.makeText(ExampleAddrecipeActivity.this,"전송완료",Toast.LENGTH_SHORT).show();
                    Log.e("이미지 전송1",response.body().toString());
                    Log.e("이미지 전송2",response.body().getRes_State()+1);
                }

            }

            @Override
            public void onFailure(Call<RecipeaddItem> call, Throwable t) {
                Toast.makeText(ExampleAddrecipeActivity.this,"전송실패",Toast.LENGTH_SHORT).show();
                Log.e("오류",t.toString());

            }
        });


    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

        cursor.close();
        return path;
    }

}