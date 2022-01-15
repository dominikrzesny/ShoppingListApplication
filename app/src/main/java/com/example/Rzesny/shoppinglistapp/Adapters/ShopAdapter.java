package com.example.Rzesny.shoppinglistapp.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Rzesny.shoppinglistapp.Models.Shop;
import com.example.Rzesny.shoppinglistapp.R;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>{

    ArrayList<Shop> shops;
    int selectedPosition=-1;
    Context context;

    public ShopAdapter(ArrayList<Shop> shops, Context context) {
        this.shops = shops;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.shopNameTextView.setText(shops.get(position).shopName);
        holder.descriptionTextView.setText(shops.get(position).description);
        holder.radiusTextView.setText(String.valueOf(shops.get(position).radius) + " m");

        String longitude = String.valueOf(shops.get(position).longitude);
        String latitude = String.valueOf(shops.get(position).latitude);
        String longitudeLabel = context.getResources().getString(R.string.LongLabel);
        String latitudeLabel = context.getResources().getString(R.string.LatLabel)    ;
        holder.coordinatesTextView.setText(latitudeLabel+" " +latitude + " / "+ longitudeLabel+" " + longitude);
    }

    public Shop getShop(int position){
        return shops.get(position);
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView shopNameTextView;
        public TextView descriptionTextView;
        public TextView radiusTextView;
        public TextView coordinatesTextView;
        public LinearLayout shopRecordLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            radiusTextView = itemView.findViewById(R.id.radiusTextView);
            coordinatesTextView = itemView.findViewById(R.id.coordinatesTextView);
            shopRecordLayout = itemView.findViewById(R.id.shopRecordLayout);
            shopRecordLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),122,0,context.getResources().getString(R.string.deleteShopMenu));
        }
    }
}