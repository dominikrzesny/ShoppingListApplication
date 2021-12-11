package com.example.Rzesny.shoppinglistapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.SharedPreferencesUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.loadTheme(this);
        ThemeUtils.onActivityCreateSetTheme(this);
        SharedPreferencesUtils.loadAndSetLocale(this);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.logoImageView);
        if(ThemeUtils.cTheme==ThemeUtils.BLACK){
            imageView.setImageResource(R.drawable.list);
        }
        if(ThemeUtils.cTheme==ThemeUtils.GREY) {
            imageView.setImageResource(R.drawable.listgrey);
        }
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            UserUtils.loggedUser = (FirebaseUser)extras.get("user");
        }
        TextView loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setText(getResources().getString(R.string.LoggedAs)+ " " +UserUtils.loggedUser.getDisplayName());
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

    public void onLogoutButtonClickMethod(View view){
        showLogoutAlertDialog();
    }

    @Override
    public void onBackPressed() {
        showLogoutAlertDialog();
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

    public void showLogoutAlertDialog(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.LoggingOutDialogTitle))
                .setMessage(getResources().getString(R.string.LoggingOutDialogMessage))
                .setPositiveButton(getResources().getString(R.string.YesButton), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserUtils.loggedUser = null;
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CancelButton), null)
                .show();
    }
}