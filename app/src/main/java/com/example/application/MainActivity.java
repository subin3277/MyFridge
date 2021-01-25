package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    MainFragment mainFragment;
    CartFragment cartFragment;
    AccountFragment accountFragment;
    RecipeFragment recipeFragment;
    GroceryFragment groceryFragment;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomTab);
        mainFragment = new MainFragment();
        cartFragment = new CartFragment();
        accountFragment = new AccountFragment();
        recipeFragment = new RecipeFragment();
        groceryFragment = new GroceryFragment();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    return;
                }
                String token = task.getResult().getToken();
                Log.d("FCM LOG","FCM 토큰"+token);
            }
        });

        Intent intent =getIntent();
        String idx = intent.getStringExtra("user_idx");
        Bundle bundle = new Bundle();
        bundle.putString("user_idx",idx);
        groceryFragment.setArguments(bundle);
        recipeFragment.setArguments(bundle);
        mainFragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,mainFragment).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_main:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,mainFragment).commit();
                        return true;
                    case R.id.tab_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,recipeFragment).commit();
                        return true;
                    case R.id.tab_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,cartFragment).commit();
                        return true;
                    case R.id.tab_grocery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,groceryFragment).commit();
                        return true;
                    case R.id.tab_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,accountFragment).commit();
                        return true;
                }

                return false;
            }
        });


    }

}