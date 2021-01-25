package com.example.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GroceryFragment extends Fragment {

    private ProgressDialog progressDialog;
    private ArrayList<GrocerylistData> grolist= new ArrayList<>();
    private EditText searchkeyword;
    private RecyclerView grolistview;
    private String url ="http://54.180.140.180:3000/api/ingredients/getIngredients";
    private String SEARCH_url="http://54.180.140.180:3000/api/ingredients/searchIngredients?keyword=";
    GrocerylistAdapter adapter;
    private String ownidx;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grocery, container, false);

        Button searchbtn = view.findViewById(R.id.searchbutton);
        grolistview = view.findViewById(R.id.grocerylistview);
        searchkeyword = view.findViewById(R.id.searchgrocery);
        ImageButton chatbot = view.findViewById(R.id.grocery_chatbot);

        grolist.clear();

        Bundle bundle1 = getArguments();
        ownidx=bundle1.getString("user_idx");

        Log.e("intent","받아온 useridx:"+ownidx);

        adapter = new GrocerylistAdapter(grolist,getContext());
        grolistview.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        grolistview.setLayoutManager(gridLayoutManager);

        new GetContacts().execute();

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AIchatbotActivity.class);
                startActivity(intent);
            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = searchkeyword.getText().toString();

                grolist.clear();
                url=SEARCH_url+keyword;
                new GetContacts().execute();

                adapter.setOnItemClicklistener(new OnPersonItemClickListener_gro() {
                    @Override
                    public void onItemClick(GrocerylistAdapter.VH holder, View view, int position) {

                        GrocerylistData item =adapter.getItem(position);

                        int idx = item.getIdx();
                        String name = item.getname();

                        Bundle bundle = new Bundle();
                        bundle.putString("idx",idx+"");
                        bundle.putString("name",name);
                        bundle.putString("user_idx",ownidx);

                        Intent intent = new Intent(getContext(),groceryinfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


            }
        });

        adapter.setOnItemClicklistener(new OnPersonItemClickListener_gro() {
            @Override
            public void onItemClick(GrocerylistAdapter.VH holder, View view, int position) {

                GrocerylistData item =adapter.getItem(position);

                int idx = item.getIdx();
                String name = item.getname();

                Bundle bundle = new Bundle();
                bundle.putString("idx",idx+"");
                bundle.putString("name",item.getname());
                bundle.putString("user_idx",ownidx);

                Intent intent = new Intent(getContext(),groceryinfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


     return view;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("res_Data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        int id = c.getInt("idx");
                        String name = c.getString("name");


                        // adding contact to contact list
                        grolist.add(new GrocerylistData(id,name));
                    }
                } catch (final JSONException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Couldn't get json from server.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            adapter = new GrocerylistAdapter(grolist,getContext());
            grolistview.setAdapter(adapter);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            grolistview.setLayoutManager(gridLayoutManager);

            adapter.setOnItemClicklistener(new OnPersonItemClickListener_gro() {
                @Override
                public void onItemClick(GrocerylistAdapter.VH holder, View view, int position) {

                    GrocerylistData item =adapter.getItem(position);

                    int idx = item.getIdx();
                    String name = item.getname();

                    Bundle bundle = new Bundle();
                    bundle.putString("idx",idx+"");
                    bundle.putString("name",item.getname());
                    bundle.putString("user_idx",ownidx);

                    Intent intent = new Intent(getContext(),groceryinfoActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

}
