package com.example.Rzesny.shoppinglistapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.Rzesny.shoppinglistapp.Interfaces.ProductQueries;
import com.example.Rzesny.shoppinglistapp.Models.Product;

@Database(entities = {Product.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductQueries productQueries();
}
