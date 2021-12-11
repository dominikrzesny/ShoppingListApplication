package com.example.Rzesny.shoppinglistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductDialog extends AppCompatDialogFragment {

    private String Title;
    private boolean isUpdate = false;
    private Product product;
    private EditText productNameEditText;
    private EditText amountEditText;
    private EditText priceEditText;
    private CheckBox IsBoughtCheckBox;
    private Activity activity;
    private DatabaseReference databaseReference;

    public AddProductDialog(Product product, boolean isUpdate, Activity activity){
        this.product = product;
        this.isUpdate = isUpdate;
        this.activity = activity;
    }
    public AddProductDialog(Activity activity){
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Title = getResources().getString(R.string.AddProductButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product,null);
        productNameEditText = view.findViewById(R.id.ProductNameEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        IsBoughtCheckBox = view.findViewById(R.id.isBoughtCheckBoxD);

        if(isUpdate){
            productNameEditText.setText(product.productName);
            amountEditText.setText(product.amount);
            priceEditText.setText(String.valueOf(product.price));
            IsBoughtCheckBox.setChecked(product.bought);
            Title = getResources().getString(R.string.updateProductMenu);
            productNameEditText.setEnabled(false);
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
                if (productNameEditText.getText().toString().matches("")){
                    Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.MissingProductName), Toast.LENGTH_SHORT).show();
                }
                else {
                    Product p = new Product();
                    p.productName = productNameEditText.getText().toString();
                    if(!priceEditText.getText().toString().matches("")){
                        p.price = Float.parseFloat(priceEditText.getText().toString());
                    }
                    p.amount = amountEditText.getText().toString();
                    p.bought = IsBoughtCheckBox.isChecked();
                    databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Products/" + p.productName);
                    DatabaseUtils.pushProduct(databaseReference,p,activity.getBaseContext());
                }
            }
        });
        return builder.create();
    }
}
