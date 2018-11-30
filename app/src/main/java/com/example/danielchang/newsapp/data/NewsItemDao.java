package com.example.danielchang.newsapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.danielchang.newsapp.model.NewsItem;

import java.util.List;

@Dao
public interface NewsItemDao {

    @Query("SELECT * FROM news_item")
    LiveData<List<NewsItem>> loadAllNews();

    @Insert
    void insert(NewsItem newsItem);


    @Query("DELETE FROM news_item")
    void clearAll();
}
