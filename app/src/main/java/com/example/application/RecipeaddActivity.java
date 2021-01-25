package com.example.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeaddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = -1;

    MaterialEditText title,summary;
    Spinner time, member,diffi;
    Button reciadd_regi_btn, ingre_plus,cook_plus,cook_minus;
    ImageButton backbutton;
    ListView ingreaddlist;
    LinearLayout cookaddlist;
    ImageView recipepic;
    JSONObject jsonObject1, jsonObject2, jsonObject3,recipe_object;
    JSONArray jsonArray_recipe, jsonArray_ingre, jsonArray_cook;
    Uri uri_title;
    ImageButton[] btn = new ImageButton[30];
    Uri[] uri_des = new Uri[30];
    String[] editlist = new String[30];
    ArrayList<Uri> uri_description = new ArrayList<>();
    List<Integer> descriptionChk=new ArrayList<>();


    private RequestQueue queue;
    private ArrayList<HashMap<String,String>> ingrelist= new ArrayList<>();
    private ArrayList<HashMap<String, String>> cooklist = new ArrayList<>();

//api/recipe/postRecipe
    final String POST_url = "http://54.180.140.180:3000/api/test2/upload";


    int cook_maxidx=0;
    String txttime=null;
    String txtmem = null;
    String txtdiffi = null;

    public RecipeaddActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeadd);

        int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionResult == PackageManager.PERMISSION_DENIED){
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions,0);
        }

        backbutton = findViewById(R.id.imageButtonback);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title =findViewById(R.id.reciadd_title);
        summary=findViewById(R.id.reciadd_summary);
        time = findViewById(R.id.spinnertime);
        member=findViewById(R.id.spinnerpeople);
        diffi=findViewById(R.id.spinnerdif);

        ingre_plus=findViewById(R.id.reciadd_ingreadd_btn);
        cook_plus=findViewById(R.id.reciadd_cookplus_btn);
        cook_minus=findViewById(R.id.reciadd_cookmin_btn);

        ingreaddlist=findViewById(R.id.reciadd_ingre_list);
        cookaddlist=(LinearLayout)findViewById(R.id.reciadd_cook_listview);

        recipepic = findViewById(R.id.reciadd_picadd);

        reciadd_regi_btn = findViewById(R.id.reciadd_regi_btn);
        jsonObject1 = new JSONObject();
        jsonObject2 = new JSONObject();
        jsonObject3 = new JSONObject();
        jsonArray_cook = new JSONArray();
        jsonArray_ingre = new JSONArray();
        recipe_object = new JSONObject();

        descriptionChk=new ArrayList<>();

        for (int i=0;i<20;i++){
            descriptionChk.add(0);
        }
        Log.e("descriptionChk",descriptionChk.toString());

        final LinearLayout linear = (LinearLayout) View.inflate(RecipeaddActivity.this,R.layout.addsearchingredient,null);

        final ListAdapter ingre_adapter = new SimpleAdapter(RecipeaddActivity.this, ingrelist, R.layout.reciadd_ingr_list, new String[]{"ingredients_idx", "name","quantity","ingredientCategory"}, new int[]{R.id.reciadd_ingre_idx, R.id.reciadd_ingr_name,R.id.reciadd_ingr_quantity,R.id.reciadd_ingr_cate});
        ingreaddlist.setAdapter(ingre_adapter);

        ingre_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Searchingredient dialog = new Searchingredient(RecipeaddActivity.this, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                    }
                    @Override
                    public void onNegativeClick() {
                    }
                });
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        TextView txtingre_idx=(TextView) dialog.findViewById(R.id.reciadd_ingr_idx);
                        TextView txtingre_name = (TextView)dialog.findViewById(R.id.reciadd_ingr_pickname);
                        EditText txtingre_quantity= (EditText)dialog.findViewById(R.id.reciadd_ingr_quanti);
                        TextView txtingre_category = (TextView) dialog.findViewById(R.id.reciadd_ingr_cateview);

                        String ingre_idx = txtingre_idx.getText().toString();
                        String ingre_name =txtingre_name.getText().toString();
                        String ingre_quantity=txtingre_quantity.getText().toString();
                        String ingre_category = txtingre_category.getText().toString();

                        JSONObject ingre_object = new JSONObject();

                        try {
                            ingre_object.put("ingredients_idx",ingre_idx);
                            ingre_object.put("name",ingre_name);
                            ingre_object.put("quantity",ingre_quantity);
                            ingre_object.put("ingredientCategory",ingre_category);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("json", "생성한 ingredientobject"+ingre_object.toString());

                        jsonArray_ingre.put(ingre_object);

                        HashMap<String, String> ingre = new HashMap<>();

                        ingre.put("ingredients_idx",ingre_idx);
                        ingre.put("name",ingre_name);
                        ingre.put("quantity",ingre_quantity);
                        ingre.put("ingredientCategory",ingre_category);

                        ingrelist.add(ingre);
                        ingreaddlist.setAdapter(ingre_adapter);


                        dialogInterface.dismiss();
                    }
                });

            }
        });


        cook_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                LinearLayout ll = new LinearLayout(getApplicationContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 170));

                TextView tv = new TextView(getApplicationContext());
                tv.setText(Integer.toString(cook_maxidx+1));
                tv.setTextSize(30);
                tv.setLayoutParams(new LinearLayout.LayoutParams(100,LinearLayout.LayoutParams.MATCH_PARENT));

                final EditText et = new EditText(getApplicationContext());
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setId(cook_maxidx);
                et.setLayoutParams(new LinearLayout.LayoutParams(500,LinearLayout.LayoutParams.MATCH_PARENT));

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        int id = et.getId();
                        editlist[id]=et.getText().toString();
                    }
                });

                btn[cook_maxidx] = new ImageButton(getApplicationContext());
                btn[cook_maxidx].setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn[cook_maxidx].setLayoutParams(new LinearLayout.LayoutParams(200,LinearLayout.LayoutParams.MATCH_PARENT));
                btn[cook_maxidx].setId(cook_maxidx);
                btn[cook_maxidx].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        int id = view.getId();

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent,id);

                    }
                });

                ll.addView(tv);
                ll.addView(et);
                ll.addView(btn[cook_maxidx]);

                cookaddlist.addView(ll);
                cook_maxidx++;
            }
        });

        cook_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cook_maxidx==1){
                    Toast.makeText(RecipeaddActivity.this,"최소 한개 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    cook_maxidx--;

                    cooklist.remove(cook_maxidx);

                    //cookaddlist.setAdapter(cook_adapter);
                }
            }
        });

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txttime = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtmem = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        diffi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtdiffi = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        recipepic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("사진업로드","0");

                @SuppressLint("IntentReset")
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,-1);

            }
        });


        reciadd_regi_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String txttitle = title.getText().toString();
                String txtsummary = summary.getText().toString();

                if ((txttitle.equals("")) || (txtsummary.equals(""))) {
                    Toast.makeText(RecipeaddActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {

                    for (int i=19;i>=cook_maxidx;i--){
                        descriptionChk.remove(i);
                    }

                    Log.e("descriptionChk1",descriptionChk.toString());

                    jsonArray_recipe = new JSONArray();
                    try {
                        recipe_object.put("title", txttitle);
                        recipe_object.put("summary", txtsummary);
                        recipe_object.put("cookingTime", txttime);
                        recipe_object.put("servings",txtmem);
                        recipe_object.put("difficulty",txtdiffi);
                        recipe_object.put("user_idx",1);

                        jsonArray_recipe.put(recipe_object);

                        jsonObject1.put("recipe",jsonArray_recipe);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    JSONObject cook_object = new JSONObject();
                    for (int i=0;i<cook_maxidx;i++){
                        if (editlist[i]!=null){
                            cook_object = new JSONObject();

                            try {
                                cook_object.put("descriptionNbr",i+1);
                                cook_object.put("description",editlist[i]);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            jsonArray_cook.put(cook_object);
                        }

                    }

                    Log.e("json", "생성한 cookobject "+jsonArray_cook.toString());
                    try {
                        jsonObject2.put("ingredients",jsonArray_ingre);
                        jsonObject3.put("description",jsonArray_cook);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                Log.e("json", "생성한 json "+jsonObject1.toString());
                Log.e("json", "생성한 json "+jsonObject2.toString());
                Log.d("실행 결과","1");


                uploadresponse();

                Log.d("실행 결과","5");

            }

            });

    }

    private void uploadresponse(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl("http://54.180.140.180:3000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        File titleImageFile = new File(getRealPathFromURI(uri_title));
        RequestBody titleImage = RequestBody.create(MediaType.parse("image/*"),titleImageFile);
        MultipartBody.Part titleImagePart = MultipartBody.Part.createFormData("titleImage", titleImageFile.getName(), titleImage);

        MultipartBody.Part[] descriptionImagesParts = new MultipartBody.Part[uri_description.size()];

        for (int index = 0; index < uri_description.size(); index++) {
            String uri = getRealPathFromURI(uri_description.get(index));
            Log.d("TAG","requestUploadSurvey: description image " + index + " " + uri);
            File file = new File(uri);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("image/*"), file);
            descriptionImagesParts[index] = MultipartBody.Part.createFormData("descriptionImage", file.getName(), descriptionBody);
        }

        final APIsInterface apIsInterface = client.create(APIsInterface.class);
        Call<RecipeaddItem> PostResponse = null;

        PostResponse = apIsInterface.uploadImage(jsonArray_recipe.toString(),jsonArray_ingre.toString(),jsonArray_cook.toString(),titleImagePart,descriptionImagesParts, descriptionChk.toString());
        PostResponse.enqueue(new Callback<RecipeaddItem>() {
            @Override
            public void onResponse(Call<RecipeaddItem> call, Response<RecipeaddItem> response) {
                if (response.isSuccessful()){
                    Log.e("이미지 전송",response.body().getRes_Msg());
                    Toast.makeText(RecipeaddActivity.this,response.body().getRes_Msg(),Toast.LENGTH_SHORT).show();
                    if (response.body().getRes_Msg().equals("성공적으로 등록되었습니다.")){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeaddItem> call, Throwable t) {
                Toast.makeText(RecipeaddActivity.this,"전송에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                Log.e("이미지 전송",t.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("사진업로드","1");

        if (resultCode==RESULT_OK&&data!=null) {
            uri_title = data.getData();

            recipepic.setImageURI(uri_title); //수정필요

            if (requestCode != REQUEST_CODE) {
                //uri_des[requestCode] = data.getData();

                uri_description.add(data.getData());

                Uri desImage = data.getData();

                btn[requestCode].setImageURI(desImage);
                descriptionChk.set(requestCode,1);
            }

            else {
                Log.d("사진업로드", "2");
                }
            }
        else {
            Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

        cursor.close();
        return path;
    }
}