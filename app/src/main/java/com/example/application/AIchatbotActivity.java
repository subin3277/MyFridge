package com.example.application;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AIchatbotActivity extends AppCompatActivity {

    ImageButton backbutton;
    Button sendbtn;
    MaterialEditText sendtext;
    String response;

    RecyclerView recyclerView;
    AIChatbotAdapter aiChatbotAdapter;

    private ArrayList<AIChatData> chatlist= new ArrayList<>();

    private String Chat_URL = "http://54.180.140.180:3000/api/chatbot/getChatbot?keyword=";
    String keyword;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_ichatbot);

        sendbtn=findViewById(R.id.sendbutton);
        sendtext = findViewById(R.id.sendtext);
        recyclerView = findViewById(R.id.aichat_layout);

        backbutton = findViewById(R.id.chatButtonback);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chatlist.clear();
        aiChatbotAdapter = new AIChatbotAdapter(chatlist,this);
        recyclerView.setAdapter(aiChatbotAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendtext!=null){
                    String question = sendtext.getText().toString();
                    keyword=question;

                    sendtext.setText("");

                    new GetAnswer().execute();

                } else {
                    Toast.makeText(AIchatbotActivity.this,"메세지를 입력해주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @SuppressLint("StaticFieldLeak")
    private class GetAnswer extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Chat_URL+keyword);  //user_idx바꾸기

            Log.d("answer:", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONObject data = jsonObject.getJSONObject("res_Data");

                    response = data.getString("response");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            chatlist.add(new AIChatData(keyword,response));

            aiChatbotAdapter = new AIChatbotAdapter(chatlist,getApplicationContext());
            recyclerView.setAdapter(aiChatbotAdapter);

        }
    }


}