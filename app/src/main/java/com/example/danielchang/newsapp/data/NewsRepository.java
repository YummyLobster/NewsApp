package com.example.danielchang.newsapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;


import com.example.danielchang.newsapp.model.NewsItem;
import com.example.danielchang.newsapp.utilities.JsonUtils;
import com.example.danielchang.newsapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsRepository {
    private NewsItemDao newsItemDao;
    private LiveData<List<NewsItem>> allNews;

    public NewsRepository(Application application) {
        NewsDatabase database = NewsDatabase.getDatabase(application);
        newsItemDao = database.newsItemDao();
        allNews = newsItemDao.loadAllNews();
    }

    public void insert(NewsItem newsItem) {
        new InsertNewsItemAsyncTask(newsItemDao).execute(newsItem);
    }

    public void clearAll() {
        new ClearAllNewsAsyncTask(newsItemDao).execute();
    }

    public void populateDb() {
        new PopulateDbAsyncTask(newsItemDao).execute();
    }


    public LiveData<List<NewsItem>> getAllNews() {
        return allNews;
    }

    private static class InsertNewsItemAsyncTask extends AsyncTask<NewsItem, Void, Void> {
        private NewsItemDao newsItemDao;

        private InsertNewsItemAsyncTask(NewsItemDao dao) {
            this.newsItemDao = dao;
        }

        @Override
        protected Void doInBackground(NewsItem... newsItems) {
            newsItemDao.insert(newsItems[0]);
            return null;
        }
    }

    private static class ClearAllNewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewsItemDao newsItemDao;

        private ClearAllNewsAsyncTask(NewsItemDao dao) {
            this.newsItemDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newsItemDao.clearAll();
            return null;
        }
    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<NewsItem> newsItems = new ArrayList<>();

        private NewsItemDao newsItemDao;

        private PopulateDbAsyncTask(NewsItemDao dao) {
            this.newsItemDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL newsRequestUrl = NetworkUtils.buildUrl();
            try {
                String jsonNewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(newsRequestUrl);
                Log.e("Json Response", jsonNewsResponse);

                newsItemDao.clearAll();
                newsItems = JsonUtils.parseNews(jsonNewsResponse);
                for (NewsItem news : newsItems) {
                    newsItemDao.insert(news);
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }



}
