package com.example.news_aggregator;

public class News_Class
{
    public String news_author;
    public String news_headlines;
    public String news_description;
    public String news_url;
    public String news_urlImg;
    public String news_publishedAt;


    News_Class(String writer,String headlines,String desc,String link,String urlToImg,String publishedAt)
    {
        this.news_author=writer;
        this.news_headlines=headlines;
        this.news_description=desc;
        this.news_url=link;
        this.news_urlImg=urlToImg;
        this.news_publishedAt=publishedAt;
    }
    public String getNews_author()
    {
        return this.news_author;
    }

    public String getNews_headlines(){
        return news_headlines;
    }

    public String getNews_description(){
        return this.news_description;
    }

    public String getNews_url(){
        return this.news_url;
    }

    public String getNews_urlImg(){
        return this.news_urlImg;
    }

    public String getNews_publishedAt(){
        return this.news_publishedAt;
    }

    public String toString()
    {
        return "NewsArticle{" +
                "author='" + news_author + '\'' +
                ", title='" + news_headlines + '\'' +
                ", description='" + news_description + '\'' +
                ", url='" + news_url + '\'' +
                ", urlToImage='" + news_urlImg + '\'' +
                ", publishedAt='" + news_publishedAt + '\'' +
                '}';
    }

}
