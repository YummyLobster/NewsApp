package com.example.danielchang.newsapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.danielchang.newsapp.model.NewsItem;
import com.example.danielchang.newsapp.utilities.JsonUtils;
import com.example.danielchang.newsapp.utilities.NetworkUtils;
import com.example.danielchang.newsapp.data.NewsItemViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private EditText mSearchBoxEditText;

    private ProgressBar mProgressBar;
    private NewsAdapter mAdapter;
    private RecyclerView mNumberList;
    private ArrayList<NewsItem> news = new ArrayList<>();
    private NewsItemViewModel newsItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mNumberList = (RecyclerView)findViewById(R.id.news_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumberList.setLayoutManager(layoutManager);

        mNumberList.setHasFixedSize(true);

        mAdapter = new NewsAdapter(this,news);
        mNumberList.setAdapter(mAdapter);

        newsItemViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);
        //Observer
        newsItemViewModel.getAllNews().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable List<NewsItem> newsItems) {
                //Data
                mAdapter.setNews(newsItems);
            }
        });

    }

    private URL makeGithubSearchQuery() {
        //String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl();
        String urlString = githubSearchUrl.toString();
        Log.d("mycode", urlString);
        return githubSearchUrl;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            URL url = makeGithubSearchQuery();
            GithubQueryTask task = new GithubQueryTask();
            task.execute(url);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class GithubQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String githubSearchResults = "";
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("mycode", s);
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.GONE);
            news = JsonUtils.parseNews(s);
            mAdapter.mNews.addAll(news);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
