package com.example.Rzesny.shoppinglistapp.Utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.Models.Shop;
import com.example.Rzesny.shoppinglistapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DatabaseUtils {

    public static void pushProduct(DatabaseReference databaseReference, Product p, Context context, boolean isUpdate){
        databaseReference.setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(isUpdate)
                        Toast.makeText(context, context.getResources().getString(R.string.onProductUpdateMessage), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, context.getResources().getString(R.string.onProductAddMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void deleteProduct(DatabaseReference databaseReference, String productName, Context context){
        databaseReference.child(productName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, context.getResources().getString(R.string.onProductDeleteMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void deleteAllProducts(DatabaseReference databaseReference, Context context){
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, context.getResources().getString(R.string.onListClearMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void deleteShop(DatabaseReference databaseReference, String shopName, Context baseContext) {
        databaseReference.child(shopName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(baseContext, baseContext.getResources().getString(R.string.onShopDeleteMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void pushShop(DatabaseReference databaseReference, Shop shop, Context baseContext) {
        databaseReference.setValue(shop).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                        Toast.makeText(baseContext, baseContext.getResources().getString(R.string.onShopAddMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
