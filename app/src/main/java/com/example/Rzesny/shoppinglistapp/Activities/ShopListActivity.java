package com.example.Rzesny.shoppinglistapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Rzesny.shoppinglistapp.Adapters.ShopAdapter;
import com.example.Rzesny.shoppinglistapp.Dialogs.AddShopDialog;
import com.example.Rzesny.shoppinglistapp.Models.Shop;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.DatabaseUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {

    ArrayList<Shop> shops;
    RecyclerView recyclerView;
    ShopAdapter adapter;
    Button addShopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_shop_list);
        shops = new ArrayList<Shop>();
        recyclerView = findViewById(R.id.shops_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShopAdapter(shops,this);
        recyclerView.setAdapter(adapter);
        addShopButton = findViewById(R.id.addShopButton);
        addShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShopDialog dialog= new AddShopDialog(ShopListActivity.this);
                dialog.show(getSupportFragmentManager(),"Lauching dialog");
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Shops");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shops.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Shop shop = dataSnapshot.getValue(Shop.class);
                    shops.add(shop);
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

            case 122:
                Shop s = adapter.getShop(item.getGroupId());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Shops");
                DatabaseUtils.deleteShop(databaseReference,s.shopName,getBaseContext());
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
