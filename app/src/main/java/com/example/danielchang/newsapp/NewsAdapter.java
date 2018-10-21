package com.example.danielchang.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.danielchang.newsapp.model.ItemClickListener;
import com.example.danielchang.newsapp.model.News;


import java.util.ArrayList;

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    private ItemClickListener itemClickListener;
    public RecyclerViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{
    Context mContext;
    ArrayList<News> mNews;

    public NewsAdapter(Context context, ArrayList<News> news){
        this.mContext = context;
        this.mNews = news;
    }

    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.list_item, parent, shouldAttachToParentImmediately);
        NewsHolder viewHolder = new NewsHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView publishedAt;

        public NewsHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            publishedAt = (TextView) itemView.findViewById(R.id.publishedAt);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            title.setText(mNews.get(listIndex).getTitle());
            description.setText(mNews.get(listIndex).getDescription());
            publishedAt.setText(mNews.get(listIndex).getPublishedAt());

        }

        @Override
        public void onClick(View v) {
            String url = mNews.get(getAdapterPosition()).getUrl();

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            mContext.startActivity(i);
        }
    }

}