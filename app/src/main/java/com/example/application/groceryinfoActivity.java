package com.example.application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class groceryinfoActivity extends AppCompatActivity {

    ImageButton Buttonback;
    Button datebutton;
    ReturnDate returnDate;
    DatePickerDialog datePickerDialog;
    String year,month,day;
    int y,m,d;
    String idx;
    String user_idx;


    private String url = "http://54.180.140.180:3000/api/ingredients/getIngredients?idx=";
    String INFO_URL= "http://54.180.140.180:3000/api/ingredients/getIngredients?idx=";
    String PUT_REFR = "http://54.180.140.180:3000/api/ingredients/insertRefrigerator";

    TextView groceryinfoname, expiration, season,tipPurchase,tipCook,tipStorage;

    JSONObject jsonObject = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceryinfo);

        groceryinfoname =(TextView) findViewById(R.id.groceryinfoname);
        expiration =(TextView)findViewById(R.id.expiration);
        season =(TextView)findViewById(R.id.season);
        tipPurchase =(TextView)findViewById(R.id.tipPurchase);
        tipCook =(TextView)findViewById(R.id.tipCook);
        tipStorage = (TextView)findViewById(R.id.tipStorage);

        datebutton = findViewById(R.id.dateButton);
        returnDate = new ReturnDate();

        year = returnDate.returnYear();
        month = returnDate.returnMonth();
        day = returnDate.returnDay();

        Log.e("calender","오늘 날짜"+year+"-"+month+"-"+day);

        Buttonback=(ImageButton)findViewById(R.id.imageButtonback_groinfo);
        Buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        user_idx = intent.getStringExtra("user_idx");
        idx=intent.getStringExtra("idx");

        Log.e("intent","받아온 useridx:"+user_idx);

        url = INFO_URL+idx;
        new GetContacts2().execute();

        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(groceryinfoActivity.this, listener, Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                datePickerDialog.show();
            }
        });

    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            y = i;
            m = i1+1;
            d = i2;
            year = String.valueOf(y);
            month = String.valueOf(m);
            day = String.valueOf(d);

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                URL URL = new URL(PUT_REFR);
                connection = (HttpURLConnection) URL.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject.put("user_idx", 1);
                    jsonObject.put("ingredients_idx", idx);
                    jsonObject.put("buyDate", year + "-" + month + "-" + day);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("json", "냉장고 json : " + jsonObject.toString());

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();

                String response;
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);

                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        String result = (String) responseJSON.get("res_State");
                        String msg = (String) responseJSON.get("res_Msg");


                        switch (result){
                            case "sql_error":
                                Toast.makeText(groceryinfoActivity.this,msg,Toast.LENGTH_SHORT).show();
                                break;
                            case "success":
                                Toast.makeText(groceryinfoActivity.this,msg,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private class GetContacts2 extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String[] doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);

            String[] info = new String[0];
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("res_Data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        int txtid = c.getInt("idx");
                        String txtname = c.getString("name");
                        int txtexpiration = c.getInt("expiration");
                        String txtseason = c.getString("season");
                        String txttipPurchase = c.getString("tipPurchase");
                        String txttipCook = c.getString("tipCook");
                        String txttipStorage = c.getString("tipStorage");

                        info = new String[]{String.valueOf(txtid),txtname, String.valueOf(txtexpiration), txtseason, txttipPurchase, txttipCook, txttipStorage};
                    }
                } catch (final JSONException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(groceryinfoActivity.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });
                }
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Couldn't get json from server.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(groceryinfoActivity.this, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

            return info;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            groceryinfoname.setText(result[1]);
            expiration.setText(result[2]);
            season.setText(result[3]);
            tipPurchase.setText(result[4]);
            tipCook.setText(result[5]);
            tipStorage.setText(result[6]);

        }

    }

    public class ReturnDate{
        long now;
        Date date;
        SimpleDateFormat sdf;
        ReturnDate(){
            now=System.currentTimeMillis();
            date=new Date(now);
        }
        public String returnYear(){
            sdf = new SimpleDateFormat("yyyy");
            String year = sdf.format(date);
            return year;
        }
        public String returnMonth(){
            sdf = new SimpleDateFormat("MM");
            String month = sdf.format(date);
            return month;
        }
        public String returnDay(){
            sdf = new SimpleDateFormat("dd");
            String day = sdf.format(date);
            return day;
        }
    }

}