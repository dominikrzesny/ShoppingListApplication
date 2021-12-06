package com.example.Rzesny.shoppinglistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;

public class AddProductDialog extends AppCompatDialogFragment {

    private String Title;
    private boolean isUpdate = false;
    private Product product;
    private EditText productNameEditText;
    private EditText amountEditText;
    private EditText priceEditText;
    private CheckBox IsBoughtCheckBox;
    private AddProductDialogListener listener;
    private Activity activity;
    public static String PERMISSION = "com.example.Rzesny.shoppinglistapp.NEW_PRODUCT_PERMISSION";

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
        productNameEditText = view.findViewById(R.id.productNameEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        IsBoughtCheckBox = view.findViewById(R.id.isBoughtCheckBoxD);

        if(isUpdate){
            productNameEditText.setText(product.productName);
            amountEditText.setText(product.amount);
            priceEditText.setText(String.valueOf(product.price));
            IsBoughtCheckBox.setChecked(product.isBought);
            Title = getResources().getString(R.string.updateProductMenu);
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
                    p.isBought = IsBoughtCheckBox.isChecked();

                    if(isUpdate){
                        p.Id = product.Id;
                        DatabaseUtils.productQueries.updateRecord(product.Id, p.productName,p.amount,p.price,p.isBought);
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.onProductUpdateMessage), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        p.Id =(int)DatabaseUtils.productQueries.insert(p);
                        Toast.makeText(activity.getBaseContext(), getResources().getString(R.string.onProductAddMessage), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("PRODUCT_ADDED");
                        intent.putExtra("Id", p.Id);
                        intent.putExtra("ProductName", p.productName);
                        activity.sendBroadcast(intent,PERMISSION);
                    }

                    listener.refreshList(true);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddProductDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AddProductDialogListener");
        }
    }

    public interface AddProductDialogListener{
        void refreshList(boolean addProductResult);
    }
}
