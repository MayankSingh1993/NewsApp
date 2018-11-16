package com.example.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mayank on 11/10/18 at 6:01 PM
 **/
public class NewsAdapter extends ArrayAdapter<News> {


    NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);

    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        News news = getItem(position);

        /*
          setting the news data to listView texts
         */

        TextView webTitle = listItemView.findViewById(R.id.web_title);
        assert news != null;
        webTitle.setText(news.getWebTitle());


        TextView sectionName = listItemView.findViewById(R.id.section_name);
        sectionName.setText(news.getSectionName());


        TextView dateTextView = listItemView.findViewById(R.id.date);
        dateTextView.setText(news.getDate());

        TextView timeTextView = listItemView.findViewById(R.id.time);
        timeTextView.setText(news.getTime());

        TextView authorNameTextView = listItemView.findViewById(R.id.author_name);
        authorNameTextView.setText(news.getAuthorName());


        return listItemView;
    }
}
