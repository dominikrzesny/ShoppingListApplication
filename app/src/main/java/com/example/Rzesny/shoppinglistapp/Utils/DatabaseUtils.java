package com.example.Rzesny.shoppinglistapp.Utils;

import android.content.Context;

import androidx.room.Room;

import com.example.Rzesny.shoppinglistapp.AppDatabase;
import com.example.Rzesny.shoppinglistapp.Interfaces.ProductQueries;

public class DatabaseUtils {

    public static AppDatabase db;
    public static ProductQueries productQueries;

    public static void createDatabaseInstance(Context contex){

        if(db==null){
            db = Room.databaseBuilder(contex.getApplicationContext(),
            AppDatabase.class, "ShoppingListApp-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            productQueries = db.productQueries();
        }
        else {
            return;
        }

    };
}
