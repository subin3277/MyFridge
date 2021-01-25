package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    Button recipeaddbtn;
    private ArrayList<recipeitem> items=new ArrayList<>();
    private ArrayList<recipeitem_recomm> items2=new ArrayList<>();
    private EditText searchkeyword;
    RecyclerView recyclerView, recyclerView2;
    RecipelistAdapter adapter;
    RecipelistAdapter_recomm adapter_recomm;

    private String URL ="http://54.180.140.180:3000/api/recipe/getRecipe";
    private String SEARCH_url="http://54.180.140.180:3000/api/recipe/searchRecipe?keyword=";
    private String RECO_url="http://54.180.140.180:3000/api/recipe/getRecommendedRecipe?idx=";
    String keyword="";

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe,container,false);


        Button searchbtn = view.findViewById(R.id.searchrecipebtn);
        recyclerView = view.findViewById(R.id.recipelistview);
        searchkeyword = view.findViewById(R.id.searchrecipe);
        recyclerView2 = view.findViewById(R.id.viewreciperecom);

        items.clear();

        adapter=new RecipelistAdapter(items,getContext());
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter_recomm = new RecipelistAdapter_recomm(items2,getContext());
        recyclerView2.setAdapter(adapter_recomm);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(layoutManager);


        Bundle bundle = getArguments();
        final String ownidx=bundle.getString("user_idx");

        Log.e("intent","받아온 useridx:"+ownidx);

        readRss(ownidx);


        recipeaddbtn = (Button) view.findViewById(R.id.recipeaddbtn);
        recipeaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeaddActivity.class);  // 위치 바꾸기
                startActivity(intent);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = searchkeyword.getText().toString();

                items2.clear();
                items.clear();
                URL=SEARCH_url+keyword;
                readRss(ownidx);


                adapter= new RecipelistAdapter(items,getContext());
                recyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                recyclerView.setLayoutManager(gridLayoutManager);

                adapter.setOnItemClicklistener(new OnPersonItemClickListener() {
                    @Override
                    public void onItemClick(RecipelistAdapter.VH holder, View view, int position) {
                        recipeitem item = adapter.getItem(position);

                        Bundle bundle = new Bundle();
                        bundle.putInt("idx",Integer.parseInt(item.getIdx()));
                        bundle.putString("name",item.getTitle());
                        bundle.putString("titleImage",item.getImgURL());
                        bundle.putString("user_idx",ownidx);

                        Intent intent1 = new Intent(getContext(),recipeinfoActivity.class);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                    }
                });

            }
        });


        adapter.setOnItemClicklistener(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(RecipelistAdapter.VH holder, View view, int position) {
                recipeitem item = adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putInt("idx",Integer.parseInt(item.getIdx()));
                bundle.putString("name",item.getTitle());
                bundle.putString("titleImage",item.getImgURL());
                bundle.putString("user_idx",ownidx);

                Intent intent1 = new Intent(getContext(),recipeinfoActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter_recomm.setOnItemClicklistener_recomm(new OnPersonItemClickListener_recomm() {
            @Override
            public void onItemClick(RecipelistAdapter_recomm.VH holder, View view, int position) {
                recipeitem_recomm item2 = adapter_recomm.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putInt("idx",Integer.parseInt(item2.getIdx()));
                bundle.putString("name",item2.getTitle());
                bundle.putString("titleImage",item2.getImgURL());
                bundle.putString("user_idx",ownidx);

                Intent intent = new Intent(getContext(),recipeinfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return view;
    }


    void readRss(String ownidx){
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(URL);

        Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("res_Data");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String idx=c.getString("idx");
                    String name = c.getString("title");
                    String titleImage = c.getString("titleImage");

                    items.add(new recipeitem(idx,name,titleImage));

                }
            } catch (final JSONException e) {
                Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
            }
        }

        String jsonStr2 =sh.makeServiceCall(RECO_url+ownidx);

        Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr2);
        if (jsonStr2!=null){
            try {
                JSONObject jsonObj2 = new JSONObject(jsonStr2);

                JSONArray contants2 = jsonObj2.getJSONArray("res_Data");
                for (int i=0;i<contants2.length();i++){
                    JSONObject c2 = contants2.getJSONObject(i);

                    String idx2= c2.getString("recipe_idx");
                    String name2 = c2.getString("title");
                    String titleImage2=c2.getString("titleImage");

                    items2.add(new recipeitem_recomm(idx2,name2,titleImage2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}