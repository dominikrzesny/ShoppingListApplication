package com.example.Rzesny.shoppinglistapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.GeofenceUtils;
import com.example.Rzesny.shoppinglistapp.Utils.SharedPreferencesUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
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

        GeofenceUtils.requestLocationsPermissions(this);
        GeofenceUtils.createGeofencingClient(this);

    }

    public void onSettingButtonClickMethod(View view){
        final Intent settingsIntent = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(settingsIntent);
    }

    public void onMyListButtonClickMethod(View view){
        final Intent myListIntent = new Intent(MainActivity.this, ProductListActivity.class);
        startActivity(myListIntent);
    }

    public void onShopsListButtonClickMethod(View view){
        final Intent shopsListIntent = new Intent(MainActivity.this, ShopListActivity.class);
        startActivity(shopsListIntent);
    }

    public void onExitButtonClickMethod(View view){
        finish();
        System.exit(0);
    }

    public void onLogoutButtonClickMethod(View view){
        showLogoutAlertDialog();
    }

    public void onMapButtonClickMethod(View view) {
        if (GeofenceUtils.checkUserLocationPermissions(this)) {
            final Intent myListIntent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(myListIntent);
        }
        else{
            Toast.makeText(getBaseContext(), getResources().getString(R.string.NoPermissionsError), Toast.LENGTH_SHORT).show();
        }
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.ChannelName);
            String description = getString(R.string.ChannelDescription);
            String channelID = getString(R.string.ChannelID);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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