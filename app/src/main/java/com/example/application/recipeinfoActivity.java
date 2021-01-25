package com.example.application;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class recipeinfoActivity extends AppCompatActivity {

    String URL;
    String INFO_URL="http://54.180.140.180:3000/api/recipe/getRecipe?idx=";
    ImageButton backbbtn;
    String POSTRating_URL = "http://54.180.140.180:3000/api/rating/postRating";
    String GETRating_URL = "http://54.180.140.180:3000/api/rating/getRating?user_idx=";
    String RecipetoCart = "http://54.180.140.180:3000/api/recipe/postRecipeToBasket";

    private ArrayList<RecipeCookItem> items=new ArrayList<>();
    private ArrayList<HashMap<String,String>> ingrelist=new ArrayList<>();

    TextView recipeinfotitle, summary,cookingtime,servings,difficulty;
    ListView ingredientslist;
    ImageView recipeimage;
    RecipeCookAdapter cookadapter;
    RecyclerView recyclerView;
    RatingBar ratingBar;
    int idx,rating;
    String user_idx,recipe_idx;
    Button cartbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeinfo);

        recipeinfotitle = (TextView) findViewById(R.id.recipeinfotitle);
        summary=(TextView) findViewById(R.id.reci_summary);
        cookingtime=(TextView)findViewById(R.id.reci_cookingtime);
        servings=(TextView) findViewById(R.id.reci_servings);
        difficulty=(TextView)findViewById(R.id.reci_difficulty);
        recipeimage=(ImageView)findViewById(R.id.recipeimage);
        cartbtn= findViewById(R.id.addcartbutton);

        ingredientslist=(ListView)findViewById(R.id.recipeingre_listview);
        recyclerView = (RecyclerView) findViewById(R.id.recipecook_cardview);

        backbbtn=(ImageButton) findViewById(R.id.imageButtonback_recipeinfo);
        backbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ratingBar=findViewById(R.id.recipeinfo_ratingbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        idx = bundle.getInt("idx"+"");
        String name =bundle.getString("name");
        String imageURL=bundle.getString("titleImage");
        user_idx = bundle.getString("user_idx");


        URL = INFO_URL+idx;
        new readRss().execute();
        new getRating().execute();



        if (rating ==1||rating ==2||rating==3||rating==4||rating==5){

        }
        else {
            ratingBar.setOnRatingBarChangeListener(new ratingListener());
        }

        ingredientslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txtviewidx = (TextView) view.findViewById(R.id.reci_ingre_idx);
                TextView txtviewname = (TextView) view.findViewById(R.id.reci_ingre_title);


                Bundle bundle2 = new Bundle();
                bundle2.putInt("idx", Integer.parseInt(txtviewidx.getText().toString()));
                bundle2.putString("name",txtviewname.getText().toString());

                Intent intent = new Intent(recipeinfoActivity.this,groceryinfoActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });

        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpURLConnection httpHandler = null;
                OutputStream outputStream = null;
                InputStream inputStream = null;
                ByteArrayOutputStream baos=null;

                try {
                    URL URL = new URL(RecipetoCart);
                    httpHandler = (HttpURLConnection) URL.openConnection();
                    httpHandler.setRequestMethod("POST");
                    httpHandler.setRequestProperty("Cache-Control", "no-cache");
                    httpHandler.setRequestProperty("Content-Type", "application/json");
                    httpHandler.setRequestProperty("Accept", "application/json");
                    httpHandler.setDoOutput(true);
                    httpHandler.setDoInput(true);

                    JSONObject tocart = new JSONObject();

                    try {

                        tocart.put("user_idx",1); //user_idx 수정
                        tocart.put("recipe_idx",idx);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("json", "생성한 ratingjson"+tocart.toString());

                    outputStream = httpHandler.getOutputStream();
                    outputStream.write(tocart.toString().getBytes());
                    outputStream.flush();

                    String response;
                    int responseCode = httpHandler.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpHandler.getInputStream();
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
                                case "sql_error":
                                    Toast.makeText(recipeinfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    break;
                                case "success":
                                    Toast.makeText(recipeinfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class ratingListener implements RatingBar.OnRatingBarChangeListener{

        @Override
        public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser){
            rating=ratingBar.getRating();
            ratingBar.setRating(rating);

            HttpURLConnection httpHandler = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos=null;

            try {
                URL URL = new URL(POSTRating_URL);
                httpHandler = (HttpURLConnection) URL.openConnection();
                httpHandler.setRequestMethod("POST");
                httpHandler.setRequestProperty("Cache-Control", "no-cache");
                httpHandler.setRequestProperty("Content-Type", "application/json");
                httpHandler.setRequestProperty("Accept", "application/json");
                httpHandler.setDoOutput(true);
                httpHandler.setDoInput(true);

                JSONObject likesjsonObj = new JSONObject();

                try {

                    likesjsonObj.put("user_idx",1); //user_idx 수정
                    likesjsonObj.put("recipe_idx",idx);
                    likesjsonObj.put("rating",rating);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("json", "생성한 ratingjson"+likesjsonObj.toString());

                outputStream = httpHandler.getOutputStream();
                outputStream.write(likesjsonObj.toString().getBytes());
                outputStream.flush();

                String response;
                int responseCode = httpHandler.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpHandler.getInputStream();
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
                            case "sql_error":
                                Toast.makeText(recipeinfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "success":
                                Toast.makeText(recipeinfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private class getRating extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(GETRating_URL+1+"&recipe_idx="+recipe_idx);

            Log.e("json2", jsonStr.toString());

            if (jsonStr!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("res_Data");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);

                        rating = c.getInt("rating");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ratingBar.setRating(rating);
        }
    }

    private class readRss extends AsyncTask<Void,Void,String[]>{

        @Override
        protected String[] doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);

            Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);

            String[] info1 = new String[0];
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject jsonObj2 =jsonObj.getJSONObject("res_Data");
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj2.getJSONArray("recipe");


                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        recipe_idx=c.getString("idx");
                        String name = c.getString("title");
                        String titleImage = c.getString("titleImage");
                        String summary = c.getString("summary");
                        String cookingtime=c.getString("cookingTime");
                        String servings=c.getString("servings");
                        String difficulty = c.getString("difficulty");

                        info1= new String[]{recipe_idx,name,summary,cookingtime,servings,difficulty,titleImage};
                    }

                    JSONArray ingre = jsonObj2.getJSONArray("ingredients");
                    for (int i=0;i<ingre.length();i++){
                        JSONObject ing = ingre.getJSONObject(i);

                        String ingidx=ing.getString("ingredients_idx");
                        String ingrname = ing.getString("name");
                        String ingrquan=ing.getString("quantity");
                        String ingrcate=ing.getString("ingredientCategory");

                        HashMap<String, String> ingredients = new HashMap<>();

                        // adding each child node to HashMap key => value
                        ingredients.put("ingredients_idx",ingidx);
                        ingredients.put("name", ingrname);
                        ingredients.put("quantity", ingrquan);
                        ingredients.put("ingredientCategory",ingrcate);

                        // adding contact to contact list
                        ingrelist.add(ingredients);
                    } //레시피-재료 리스트

                    JSONArray cooking = jsonObj2.getJSONArray("description");
                    for (int i=0;i<cooking.length();i++){
                        JSONObject cooks = cooking.getJSONObject(i);

                        String cooknum = cooks.getString("descriptionNbr");
                        String cookurl = cooks.getString("descriptionImage");
                        String cookdes = cooks.getString("description");

                        items.add(new RecipeCookItem(cooknum,cookdes,cookurl));
                    } //레시피- 조리과정 리스트

                } catch (final JSONException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
                }


            }
            return info1;
        }
        @Override
        protected void onPostExecute(String[] info1) {
            super.onPostExecute(info1);

            recipeinfotitle.setText(info1[1]);
            summary.setText(info1[2]);
            cookingtime.setText(info1[3]);
            servings.setText(info1[4]);
            difficulty.setText(info1[5]);

            String imageURL = info1[6];
            Glide.with(recipeinfoActivity.this).load(imageURL).into(recipeimage);

            ListAdapter adapter = new SimpleAdapter(recipeinfoActivity.this, ingrelist, R.layout.recipe_ingre_list, new String[]{"ingredients_idx","name", "quantity","ingredientCategory"}, new int[]{R.id.reci_ingre_idx,R.id.reci_ingre_title, R.id.reci_ingre_quan,R.id.reci_ingre_cate});
            ingredientslist.setAdapter(adapter);

            cookadapter=new RecipeCookAdapter(items,recipeinfoActivity.this);
            recyclerView.setAdapter(cookadapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(recipeinfoActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);

        }
    }

}