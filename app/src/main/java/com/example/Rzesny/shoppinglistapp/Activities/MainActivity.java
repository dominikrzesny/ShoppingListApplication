package com.example.Rzesny.shoppinglistapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.SharedPreferencesUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.loadTheme(this);
        ThemeUtils.onActivityCreateSetTheme(this);
        SharedPreferencesUtils.loadAndSetLocale(this);
        setContentView(R.layout.activity_main);
        DatabaseUtils.createDatabaseInstance(this);
        ImageView imageView = findViewById(R.id.logoImageView);
        if(ThemeUtils.cTheme==ThemeUtils.BLACK){
            imageView.setImageResource(R.drawable.list);
        }
        if(ThemeUtils.cTheme==ThemeUtils.GREY){
            imageView.setImageResource(R.drawable.listgrey);
        }
    }

    public void onSettingButtonClickMethod(View view){
        final Intent settingsIntent = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(settingsIntent);
    }

    public void onMyListButtonClickMethod(View view){
        final Intent myListIntent = new Intent(MainActivity.this, ProductListActivity.class);
        startActivity(myListIntent);
    }

    public void onExitButtonClickMethod(View view){
        finish();
        System.exit(0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(OptionsActivity.configurationChanged){
            OptionsActivity.configurationChanged=false;
            recreate();
        }
    }
}