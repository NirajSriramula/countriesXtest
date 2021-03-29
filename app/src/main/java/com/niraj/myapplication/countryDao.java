package com.niraj.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface countryDao {
    @Insert
    void insert(country country);

    @Query("delete from country_table")
    void DeleteAll();
    @Query("SELECT * FROM COUNTRY_TABLE WHERE name= :nam")
    country getData(String nam);

    @Query("SELECT * FROM country_table")
    List<country> getAll();
}
