package com.example.Rzesny.shoppinglistapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Rzesny.shoppinglistapp.Dialogs.AddProductDialog;
import com.example.Rzesny.shoppinglistapp.Adapters.ProductAdapter;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Models.Product;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductListActivity extends AppCompatActivity
{
    ArrayList<Product> products;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    Button addProductButton;
    Button clearListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_product_list);
        products = new ArrayList<Product>();
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
                if(products.isEmpty()){
                    return;
                }
                DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Products");
                DatabaseUtils.deleteAllProducts(databaseReference,getBaseContext());
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case 120:
                Product p = adapter.getProduct(item.getGroupId());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Products");
                DatabaseUtils.deleteProduct(databaseReference,p.productName,getBaseContext());
                return true;
            case 121:
                Product product = adapter.getProduct(item.getGroupId());
                AddProductDialog dialog= new AddProductDialog(product,true,this);
                dialog.show(getSupportFragmentManager(),"Lauching dialog");
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
