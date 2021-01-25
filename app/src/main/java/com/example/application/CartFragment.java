package com.example.application;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CartFragment extends Fragment {

    private ArrayList<cartitem> items = new ArrayList<>();
    private ArrayList<carttorefriitem> cartlist = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView cartview;
    DatePickerDialog datePickerDialog;
    String year,month,day;
    ReturnDate returnDate;
    int y,m,d;
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();

    EditText search;
    Button searchBtn, sendbtn;
    CarttoRefrilistAdapter listAdapter;


    private String CART_URL="http://54.180.140.180:5000/api/crawling/getSSG?keyword=";
    private String CART_LIST_URL = "http://54.180.140.180:3000/api/basket/getBasket?user_idx=";
    private String CART_TOREFRI_URL="http://54.180.140.180:3000/api/basket/postBasketToRefrigerator";
    String keyword="";


    public CartFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart,container,false);

        recyclerView= view.findViewById(R.id.cartlistview);
        search = view.findViewById(R.id.cart_searchbar);
        searchBtn=view.findViewById(R.id.cart_searchbutton);
        cartview=view.findViewById(R.id.cartview);
        sendbtn = view.findViewById(R.id.cart_carttorefri_btn);

        returnDate = new ReturnDate();

        year = returnDate.returnYear();
        month = returnDate.returnMonth();
        day = returnDate.returnDay();

        listAdapter=new CarttoRefrilistAdapter(cartlist,getContext());
        recyclerView.setAdapter(listAdapter);

        new Getcartlist().execute();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(getContext(), listener, Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                datePickerDialog.show();
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = search.getText().toString();

                final CartlistAdapter adapter;

                items.clear();
                readRss(keyword);

                adapter=new CartlistAdapter(items,getContext());
                recyclerView.setAdapter(adapter);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                recyclerView.setLayoutManager(gridLayoutManager);

                adapter.setOnItemClicklistener(new OnPersonItemClickListener_cart() {
                    @Override
                    public void onItemClick(CartlistAdapter.VH holder, View view, int position) {
                        cartitem cartitem =adapter.getItem(position);

                        String link = cartitem.getLink();

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);
                    }
                });

            }
        });





        return view;
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
                URL URL = new URL(CART_TOREFRI_URL);
                connection = (HttpURLConnection) URL.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject.put("user_idx", 1);  //user_idx 수정
                    jsonObject.put("buyDate", year + "-" + month + "-" + day);
                    jsonObject.put("shopping_list", jsonArray);

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
                                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                                break;
                            case "success":
                                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
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

    @SuppressLint("StaticFieldLeak")
    private class Getcartlist extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(CART_LIST_URL + 1);  //user_idx바꾸기

            Log.d("cart_list:", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray data = jsonObject.getJSONArray("res_Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        String ingredients_idx = object.getString("ingredients_idx");
                        String ingredients_name = object.getString("ingredients_name");


                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("ingredients_idx", ingredients_idx);
                        contact.put("ingredients_name", ingredients_name);

                        cartlist.add(new carttorefriitem(ingredients_idx,ingredients_name));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            listAdapter=new CarttoRefrilistAdapter(cartlist,getContext());
            cartview.setAdapter(listAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            cartview.setLayoutManager(layoutManager);


            listAdapter.setOnItemClicklistener(new OnPersonItemClickListener_carttorefri() {
                @Override
                public void onItemClick(CarttoRefrilistAdapter.VH holder, View view, int position) {
                    carttorefriitem carttorefriitem = listAdapter.getItem(position);

                    String ingre_idx=carttorefriitem.getIngre_idx();

                    JSONObject shoppinglist = new JSONObject();

                    try {
                        shoppinglist.put("ingredients_idx",ingre_idx);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    jsonArray.put(shoppinglist);

                    Log.e("jsonArray",jsonArray.toString());
                }
            });

        }
    }

    void readRss(String keyword){
        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(CART_URL+keyword);

        if (jsonStr!=null){
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                Log.d("cart_json:",jsonStr);

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);

                    String image=c.getString("image");
                    String link=c.getString("link");
                    String name=c.getString("name");
                    String price=c.getString("price");

                    items.add(new cartitem(image,link,name,price+"원"));
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
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