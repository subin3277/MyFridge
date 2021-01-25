package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class signupActivity extends AppCompatActivity {

    MaterialEditText username, nickname, email, password, password2, memnum;
    Button signupbutton;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        //아이디 값 찾기
        username = findViewById(R.id.editname_signup);
        nickname = findViewById(R.id.editnickname_signup);
        email = findViewById(R.id.editemail_signup);
        password = findViewById(R.id.editpassword_signup);
        password2 = findViewById(R.id.editpassword2_signup);
        memnum = findViewById(R.id.editmemnum);

        final String url = "http://54.180.140.180:3000/api/user/signup";

        signupbutton = findViewById(R.id.signupbutton);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtemail = email.getText().toString();
                String txtusername = username.getText().toString();
                String txtnickname = nickname.getText().toString();
                String txtpassword = password.getText().toString();
                String txtpassword2 = password2.getText().toString();
                String txtmemnum = memnum.getText().toString();

                if ((txtemail.equals(""))||(txtpassword.equals(""))||(txtpassword2.equals(""))||(txtnickname.equals(""))){
                    Toast.makeText(signupActivity.this,"모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                else {
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
                            jsonObject.put("id",txtemail);
                            jsonObject.put("pw",txtpassword);
                            jsonObject.put("pw2",txtpassword2);
                            jsonObject.put("nickName",txtnickname);
                            jsonObject.put("userName",txtusername);
                            jsonObject.put("memNum",txtmemnum);

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

                                switch (result) {
                                    case "id_is_invalid_form":
                                        Toast.makeText(signupActivity.this, "올바른 이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "pw_is_invalid_form":
                                        Toast.makeText(signupActivity.this, "올바른 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "nickName_is_invalid_form":
                                        Toast.makeText(signupActivity.this, "올바른 닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "userName_is_invalid_form":
                                        Toast.makeText(signupActivity.this, "올바른 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "pw_do_not_match":
                                        Toast.makeText(signupActivity.this, "두 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "email_is_already_used":
                                        Toast.makeText(signupActivity.this, "사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "nickname_is_already_used":
                                        Toast.makeText(signupActivity.this, "사용중인 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "success":
                                        Toast.makeText(signupActivity.this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(signupActivity.this, loginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(signupActivity.this, "회원가입에 실패했습니다..", Toast.LENGTH_SHORT).show();
                                        break;
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
        });
    }
}






