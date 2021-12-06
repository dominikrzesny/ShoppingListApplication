package com.example.Rzesny.shoppinglistapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Rzesny.shoppinglistapp.Models.Product;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{


    ArrayList<Product> products;
    int selectedPosition=-1;
    Context context;

    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
     holder.productNameTextView.setText(products.get(position).productName);
     holder.priceTextView.setText(String.valueOf(products.get(position).price) + " zł");
     holder.amountTextView.setText(products.get(position).amount);
     holder.isBoughtCheckBox.setChecked(products.get(position).isBought);
    }

    public Product getProduct(int position){
        return products.get(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView productNameTextView;
        public TextView priceTextView;
        public TextView amountTextView;
        public CheckBox isBoughtCheckBox;
        public LinearLayout productRecordLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            isBoughtCheckBox = itemView.findViewById(R.id.isBoughtCheckBox);
            productRecordLayout = itemView.findViewById(R.id.productRecordLayout);
            productRecordLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),120,0,context.getResources().getString(R.string.deleteProductMenu));
            menu.add(this.getAdapterPosition(),121,1,context.getResources().getString(R.string.updateProductMenu));
        }
    }
}
