package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends Fragment {

    private ArrayList<HashMap<String, String>> refrilist,expirlist;
    private ListView refrilistview, expirlistview;
    private String user_idx;
    private String REFRI_URL="http://54.180.140.180:3000/api/refrigerator/getRefrigerator?user_idx=";
    private String TEMP_URL="http://54.180.140.180:3000/api/IOT/getTempHumi?user_idx=";
    private String EXPIR_URL="http://54.180.140.180:3000/api/refrigerator/getExpiratedIngredients?user_idx=";

    Button tempbtn,checkbtn;
    TextView temp,humi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main,container,false);

        refrilistview = view.findViewById(R.id.main_refri_listview);
        expirlistview = view.findViewById(R.id.main_expir_listview);
        tempbtn = view.findViewById(R.id.main_tembtn);
        checkbtn = view.findViewById(R.id.main_checkref);
        temp = view.findViewById(R.id.main_temp);
        humi = view.findViewById(R.id.main_humidty);


        refrilist = new ArrayList<>();
        expirlist = new ArrayList<>();

        final Bundle bundle = getArguments();
        user_idx = bundle.getString("user_idx");

        new GetContants().execute();
        new GetExpirated().execute();

        tempbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetTemp().execute();

            }
        });

        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),MainPopActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }



    @SuppressLint("StaticFieldLeak")
    private class GetTemp extends AsyncTask<Void,Void,Void>{  //온습도 가져오기

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler hh = new HttpHandler();
            String jsonRES = hh.makeServiceCall(TEMP_URL+1);

            if (jsonRES!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonRES);
                    JSONArray jsonArray = jsonObject.getJSONArray("res_Data");

                    JSONObject c = jsonArray.getJSONObject(0);

                    String temperature = c.getString("temperature");
                    String humidity = c.getString("humidity");

                    temp.setText(temperature+"℃");
                    humi.setText(humidity+"%");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetExpirated extends AsyncTask<Void,Void,Void> {  //임박 식재료 가져오기

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(EXPIR_URL + 1);  //user_idx바꾸기

            Log.d("expir_json:", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray data = jsonObject.getJSONArray("res_Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        String ingredients_name = object.getString("ingredients_name");
                        String buyDate = object.getString("buyDate");
                        String expirationDate = object.getString("expirationDate");
                        String expiration = object.getString("expiration");

                        String[] date = buyDate.split("T");
                        String[] expira = expirationDate.split("T");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("ingredients_name", ingredients_name);
                        contact.put("buyDate", date[0]);
                        contact.put("expirationDate", expira[0]);
                        contact.put("expiration", expiration);

                        expirlist.add(contact);
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

            ListAdapter adapter = new SimpleAdapter(getActivity(),expirlist,R.layout.main_refrigerator_expiralist,new String[]{"ingredients_name","buyDate","expirationDate","expiration"},
                    new int[]{R.id.refriexpri_ingre_name,R.id.refriexpri_buydate,R.id.refriexpri_expri_date,R.id.refriexpri_expri});

            expirlistview.setAdapter(adapter);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetContants extends AsyncTask<Void,Void,Void>{  //식재료 가져오기


        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(REFRI_URL+1);  //user_idx바꾸기

            Log.d("refri_json:",jsonStr);

            if (jsonStr!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray data = jsonObject.getJSONArray("res_Data");
                    for (int i = 0;i<data.length();i++){
                        JSONObject object = data.getJSONObject(i);

                        String ingredients_name = object.getString("ingredients_name");
                        String buyDate = object.getString("buyDate");
                        String expirationDate = object.getString("expirationDate");
                        String expiration = object.getString("expiration");

                        String[] date =buyDate.split("T");
                        String[] expira = expirationDate.split("T");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("ingredients_name",ingredients_name);
                        contact.put("buyDate",date[0]);
                        contact.put("expirationDate",expira[0]);
                        contact.put("expiration",expiration);

                        refrilist.add(contact);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(getActivity(),refrilist,R.layout.main_refrigerator_list,new String[]{"ingredients_name","buyDate","expirationDate","expiration"},
                    new int[]{R.id.refri_ingre_name,R.id.refri_buydate,R.id.refri_expri_date,R.id.refri_expri});


            refrilistview.setAdapter(adapter);
        }
    }
}