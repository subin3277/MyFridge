package com.example.application;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Searchingredient extends Dialog{

    private Context context;

    ListView searchingreaddlist;
    private String SEARCH_url="http://54.180.140.180:3000/api/ingredients/searchIngredients?keyword=";
    String SEARCH_ingre_url="";

    private ArrayList<HashMap<String,String>> search_ingrelist = new ArrayList<>();
    private String txtviewidx,txtviewname;
    private TextView pickname,category,idx;
    private MaterialEditText editquantitiy;
    private Spinner spinner;
    Button plus,cancel,search_btn;

    private CustomDialogClickListener customDialogClickListener;

    String txtname,txtquantity,txtcategory;

    public Searchingredient(Context context,CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context=context;
        this.customDialogClickListener=customDialogClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsearchingredient);

        final MaterialEditText search = findViewById(R.id.search_ingre_text);
        searchingreaddlist =findViewById(R.id.ingre_search_list);
        search_btn = findViewById(R.id.ingre_search_btn);

        pickname = findViewById(R.id.reciadd_ingr_pickname);
        editquantitiy = findViewById(R.id.reciadd_ingr_quanti);
        spinner = findViewById(R.id.reciadd_ingr_category);
        category = findViewById(R.id.reciadd_ingr_cateview);

        plus = findViewById(R.id.reciadd_ingr_btn);
        cancel = findViewById(R.id.reciadd_ingr_canbtn);
        idx=findViewById(R.id.reciadd_ingr_idx);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = search.getText().toString();
                search_ingrelist.clear();
                SEARCH_ingre_url = SEARCH_url+keyword;

                new searchingre().execute();
            }
        });

        searchingreaddlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewidx = (TextView) view.findViewById(R.id.groceryidx);
                TextView viewname = (TextView) view.findViewById(R.id.groceryname);

                txtviewidx = viewidx.getText().toString();
                txtviewname = viewname.getText().toString();

                pickname.setText(txtviewname);
                idx.setText(txtviewidx);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtcategory = adapterView.getItemAtPosition(i).toString();
                category.setText(txtcategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txtcategory = adapterView.getItemAtPosition(0).toString();
                category.setText(txtcategory);
            }
        });

    }

    private class searchingre extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler search = new HttpHandler();

            String search_jsonStr = search.makeServiceCall(SEARCH_ingre_url);

            Log.e(RecipeaddActivity.class.getSimpleName(), "Response from url: " + search_jsonStr);

            if (search_jsonStr != null) {
                try {
                    JSONObject search_jsonObj = new JSONObject(search_jsonStr);

                    // Getting JSON Array node
                    JSONArray search_contacts = search_jsonObj.getJSONArray("res_Data");

                    // looping through All Contacts
                    for (int i = 0; i < search_contacts.length(); i++) {
                        JSONObject se_c = search_contacts.getJSONObject(i);

                        int id = se_c.getInt("idx");
                        String name = se_c.getString("name");

                        // tmp hash map for single contact
                        HashMap<String, String> search_contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        search_contact.put("idx", id+"");
                        search_contact.put("name", name);

                        // adding contact to contact list
                        search_ingrelist.add(search_contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter search_adapter = new SimpleAdapter(context,search_ingrelist, R.layout.grocery_item_list, new String[]{"idx", "name"}, new int[]{R.id.groceryidx, R.id.groceryname});
            searchingreaddlist.setAdapter(search_adapter);
        }
    }
}
