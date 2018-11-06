package com.example.danielchang.newsapp.utilities;

import com.example.danielchang.newsapp.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<NewsItem> parseNews(String jsonResult){
        ArrayList<NewsItem> repoList = new ArrayList<>();
        try {
            JSONObject mainJSONObject = new JSONObject(jsonResult);
            JSONArray items = mainJSONObject.getJSONArray("articles");

            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                repoList.add(new NewsItem(item.getString("title"), item.getString("description"), item.getString("url"),item.getString("publishedAt")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repoList;
    }
}