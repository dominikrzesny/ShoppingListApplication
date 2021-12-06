package com.example.Rzesny.shoppinglistapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Rzesny.shoppinglistapp.AddProductDialog;
import com.example.Rzesny.shoppinglistapp.ProductAdapter;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;

import java.util.ArrayList;


public class ProductListActivity extends AppCompatActivity implements AddProductDialog.AddProductDialogListener
{
    ArrayList<Product> products;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    Button addProductButton;
    Button clearListButton;
    String deleteMenuString, updateMenuString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_product_list);
        products = new ArrayList<>();
        products = new ArrayList<Product>(DatabaseUtils.productQueries.getAll());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(products,this);
        recyclerView.setAdapter(adapter);
        addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductDialog dialog= new AddProductDialog(ProductListActivity.this);
                dialog.show(getSupportFragmentManager(),"Lauching dialog");
            }
        });
        clearListButton = findViewById(R.id.clearListButton);
        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtils.productQueries.clearTable();
                refreshList(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void refreshList(boolean result) {
        if(result){
            products = new ArrayList<Product>(DatabaseUtils.productQueries.getAll());
            adapter = new ProductAdapter(products,this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case 120:
                Product p = adapter.getProduct(item.getGroupId());
                DatabaseUtils.productQueries.delete(p);
                Toast.makeText(getBaseContext(), getResources().getString(R.string.onProductDeleteMessage), Toast.LENGTH_SHORT).show();
                this.refreshList(true);
                return true;
            case 121:
                Product product = adapter.getProduct(item.getGroupId());
                AddProductDialog dialog= new AddProductDialog(product,true,this);
                dialog.show(getSupportFragmentManager(),"Lauching dialog");
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra("Id")) {
            int productId = intent.getIntExtra("Id",0);
            Product p = DatabaseUtils.productQueries.findById(productId);
            AddProductDialog dialog= new AddProductDialog(p,true,this);
            dialog.show(getSupportFragmentManager(),"Lauching dialog");
        }
    }
}
