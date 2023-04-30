package com.example.news_aggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Locale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class News_Adapter extends RecyclerView.Adapter <News_Adapter.News_ViewHolder>
{
    private final MainActivity mActivity;

    private static final String TAG = "News_Adapter";
    private final ArrayList<News_Class> news_list;
    public Context cont;
    private int img_placeholder;
    private int img_broken;
    private int img_no_picture;
    public News_Adapter(MainActivity mainactivity, ArrayList<News_Class> newsList, Context con) {
        mActivity = mainactivity;
        news_list = newsList;
        cont =con;
    }

    public class News_ViewHolder extends RecyclerView.ViewHolder
    {
        TextView news_author;
        TextView news_headline;
        TextView news_article_date;
        TextView news_description;
        TextView news_page_num;
        ImageView news_image;

        public News_ViewHolder(View view)
        {
            super(view);
            news_author = view.findViewById(R.id.news_editor);
            news_headline = view.findViewById(R.id.news_headlines);
            news_article_date = view.findViewById(R.id.news_dateTime);
            news_description = view.findViewById(R.id.news_desc);
            news_page_num = view.findViewById(R.id.news_page_num);
            news_image = view.findViewById(R.id.news_image);
        }
    }
    @NonNull
    @Override
    public News_Adapter.News_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new News_ViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.news_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull News_Adapter.News_ViewHolder holder, int position) {
        News_Class news = news_list.get(position);
        holder.news_author.setText(news.getNews_author());
        holder.news_headline.setText(news.getNews_headlines());
        holder.news_description.setText(news.getNews_description());
        holder.news_image.setImageResource(R.drawable.loading);
        holder.news_page_num.setText(String.format(
                Locale.getDefault(),"%d of %d", (position+1), news_list.size()));

        Log.d(TAG, "onBindViewHolder: ");
        holder.news_headline.setOnClickListener(v->{
            String webUrl = news.getNews_url();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(webUrl));
            mActivity.startActivity(intent);
        });
        holder.news_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webUrl = news.getNews_url();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webUrl));
                mActivity.startActivity(intent);            }
        });
        holder.news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webUrl = news.getNews_url();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webUrl));
                mActivity.startActivity(intent);
            }
        });

        if (news.getNews_author().equals("null")) {
            holder.news_author.setVisibility(View.GONE);
        }

        if (news.getNews_headlines().equals("null")) {
            holder.news_headline.setVisibility(View.GONE);
        }

        if (news.getNews_description().equals("null")) {
            holder.news_description.setVisibility(View.GONE);
        }

        if (news.getNews_urlImg().equals("null")) {
            holder.news_image.setImageResource(R.drawable.noimage);
        }

        if (!news.getNews_publishedAt().equals("null"))
        {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = parser.parse(news.getNews_publishedAt().split("T")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
            String formattedDate = formatter.format(date);
            holder.news_article_date.setText(formattedDate +" " + news.getNews_publishedAt().split("T")[1].split(":")[0] + ":" + news.getNews_publishedAt().split("T")[1].split(":")[1]);
        }
        else
        {
            holder.news_article_date.setVisibility(View.GONE);
        }
        img_broken = cont.getResources().getIdentifier("brokenimage", "drawable", cont.getPackageName());
        img_placeholder = cont.getResources().getIdentifier("loading", "drawable", cont.getPackageName());
        img_no_picture = cont.getResources().getIdentifier("noimage", "drawable", cont.getPackageName());

        Glide.with(cont)
                .load(news_list.get(position).getNews_urlImg())
                .placeholder(img_placeholder)
                .error(news_list.get(position).getNews_urlImg().equals("")?img_no_picture:img_broken)
                .addListener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.news_image);
    }

    public int getItemCount()
    {
        return news_list.size();
    }
}