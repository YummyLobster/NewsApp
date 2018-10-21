package com.example.danielchang.newsapp.utilities;

import com.example.danielchang.newsapp.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    public static ArrayList<News> makeRepositoryList(String jsonResult){
        ArrayList<News> repoList = new ArrayList<>();
        try {
            JSONObject mainJSONObject = new JSONObject(jsonResult);
            JSONArray items = mainJSONObject.getJSONArray("articles");

            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                repoList.add(new News(item.getString("title"), item.getString("description"), item.getString("url"),item.getString("publishedAt")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repoList;
    }
}