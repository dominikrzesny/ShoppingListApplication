package com.example.Rzesny.shoppinglistapp.Models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "Name")
    public String productName;

    @ColumnInfo(name = "Price")
    public float price ;

    @ColumnInfo(name = "Amount")
    public String amount;

    @ColumnInfo(name = "isBought")
    public boolean isBought;

}
