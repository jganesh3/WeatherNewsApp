/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class myCustomNewsAdapter extends ArrayAdapter<NewsItems> {

    private Context context;
    private List<NewsItems> data;

    public myCustomNewsAdapter(Context context, int resource, List<NewsItems> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //1.create inflator
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflator.inflate(R.layout.custome_news, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.newsTitleTV);
        TextView desc = (TextView) rowView.findViewById(R.id.descTV);
        title.setText(data.get(position).getTitle());
        desc.setText(Html.fromHtml(data.get(position).getDate()));
        return rowView;


    }


}
