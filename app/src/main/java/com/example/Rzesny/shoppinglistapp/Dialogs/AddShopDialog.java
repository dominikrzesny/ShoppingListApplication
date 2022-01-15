package com.example.Rzesny.shoppinglistapp.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.example.Rzesny.shoppinglistapp.Models.Shop;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.GeofenceUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShopDialog extends AppCompatDialogFragment {

    private String Title;
    private EditText shopNameEditText;
    private EditText descriptionEditText;
    private EditText radiusEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Activity activity;
    private DatabaseReference databaseReference;

    public AddShopDialog(Activity activity){
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Title = getResources().getString(R.string.AddShopButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_shop,null);
        shopNameEditText = view.findViewById(R.id.shopNameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        radiusEditText = view.findViewById(R.id.radiusEditText);
        latitudeEditText = view.findViewById(R.id.latitudeEditText);
        longitudeEditText = view.findViewById(R.id.longitudeEditText);

        Location location = GeofenceUtils.getLastLocation(activity);
        if(location!=null){
            longitudeEditText.setText(String.valueOf(location.getLongitude()));
            latitudeEditText.setText(String.valueOf(location.getLatitude()));
        }

        builder.setView(view)
                .setTitle(Title)
                .setNegativeButton(getResources().getString(R.string.CancelButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }).setPositiveButton(getResources().getString(R.string.ConfirmButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (shopNameEditText.getText().toString().matches("")){
                    Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.MissingShopName), Toast.LENGTH_SHORT).show();
                }
                if(radiusEditText.getText().toString().matches("")){
                    Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.MissingRadius), Toast.LENGTH_SHORT).show();
                }
                else {
                    Shop s = new Shop();
                    s.shopName = shopNameEditText.getText().toString();
                    s.description = descriptionEditText.getText().toString();
                    s.radius = Integer.parseInt(radiusEditText.getText().toString());
                    s.latitude = Double.parseDouble(latitudeEditText.getText().toString());
                    s.longitude = Double.parseDouble(longitudeEditText.getText().toString());
                    databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Shops/" + s.shopName);
                    DatabaseUtils.pushShop(databaseReference,s,activity.getBaseContext());
                }
            }
        });
        return builder.create();
    }
}
