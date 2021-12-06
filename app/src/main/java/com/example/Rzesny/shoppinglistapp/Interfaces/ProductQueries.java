package com.example.Rzesny.shoppinglistapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.Rzesny.shoppinglistapp.Models.Product;

import java.util.List;

@Dao
public interface ProductQueries {

    @Query("SELECT * FROM Products")
    List<Product> getAll();

    @Query("SELECT * FROM Products WHERE id IN (:productIds)")
    List<Product> loadAllByIds(int[] productIds);

    @Query("SELECT * FROM Products WHERE Name LIKE :productName LIMIT 1")
    Product findByName(String productName);

    @Query("SELECT * FROM Products WHERE id=:Id")
    Product findById(int Id);

    @Insert
    long insert(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM Products")
    public void clearTable();

    @Query("UPDATE Products set Name=:name,Price=:price, Amount=:amount, isBought=:isBought WHERE Id=:ID")
    public void updateRecord(int ID, String name, String amount, Float price, boolean isBought);
}
