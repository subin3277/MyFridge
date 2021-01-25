package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginActivity extends AppCompatActivity {

    private Button signupbutton;
    private Button loginbutton;

    MaterialEditText id, password;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    final String url = "http://54.180.140.180:3000/api/user/signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        saveToken();

        signupbutton = findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, signupActivity.class);
                startActivity(intent);
                finish();
            }
        }); //회원등록버튼 클릭시 수행

        id = findViewById(R.id.editid);
        password = findViewById(R.id.editpassword);
        loginbutton = findViewById(R.id.button_login);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String txtid = id.getText().toString();
                final String txtpw = password.getText().toString();


                Intent intent = new Intent(loginActivity.this,MainActivity.class);
                startActivity(intent);


                if(txtid.equals("")||txtpw.equals("")){
                    Toast.makeText(loginActivity.this,"아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {

                    login(txtid,txtpw);
                }
            }
        });
    }

    public void saveToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    return;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("UserTokenKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String token = task.getResult().getToken();
                editor.putString("TokenCode",token);
                editor.apply();
            }
        });
    } //sharedpreferences에 토큰값저장해놓기.

    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserTokenKey", MODE_PRIVATE);
        return sharedPreferences.getString("TokenCode", "");
    }  //sharedpreference에서 값꺼내기


    public void login(String id,String pw){

        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos=null;

        try {
            URL URL = new URL(url);
            connection=(HttpURLConnection) URL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cache-Control","no-cache");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("id",id);
                jsonObject.put("pw",pw);
                jsonObject.put("token",getToken());

            } catch (JSONException e){
                e.printStackTrace();
            }
            Log.e("json","생성한 json"+jsonObject.toString());

            outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();

            String response;
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray(); response = new String(byteData);

                response = new String(byteData);

                try {
                    JSONObject responseJSON = new JSONObject(response);
                    String result = (String) responseJSON.get("res_State");
                    String msg = (String) responseJSON.get("res_Msg");

                    JSONArray jsonArray = responseJSON.getJSONArray("res_Data");
                    JSONObject c = jsonArray.getJSONObject(0);

                    String idx=c.getString("idx");

                    switch (result) {
                        case "invalid_data":
                            Toast.makeText(loginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                        case "success":

                            Toast.makeText(loginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginActivity.this,MainActivity.class);
                            intent.putExtra("user_idx",idx);
                            startActivity(intent);
                    }


                } catch (JSONException e){
                    e.printStackTrace();
                }

            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }


    }




