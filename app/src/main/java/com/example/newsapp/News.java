package com.example.newsapp;

/**
 * Created by Mayank on 11/10/18 at 5:52 PM
 **/
public class News {
    private String webTitle;
    private String sectionName;
    private String date;
    private String webUrl;
    private String time;
    private String authorName;

    String getAuthorName() {
        return authorName;
    }


    News(String webTitle, String sectionName, String date, String webUrl, String time, String authorName) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.date = date;
        this.webUrl = webUrl;
        this.time = time;
        this.authorName = authorName;
    }

    String getWebTitle() {
        return webTitle;
    }

    String getSectionName() {
        return sectionName;
    }

    public String getDate() {
        return date;
    }

    String getWebUrl() {
        return webUrl;
    }

    String getTime() {
        return time;
    }
}
