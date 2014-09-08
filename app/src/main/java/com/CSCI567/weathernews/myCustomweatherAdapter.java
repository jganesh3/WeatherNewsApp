/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class myCustomweatherAdapter extends ArrayAdapter<WeatherItems> {


    private Context context;
    public List<WeatherItems> data;
    private SparseBooleanArray mSelectedItemsIds;

    public myCustomweatherAdapter(Context context, int resource, List<WeatherItems> objects) {
        super(context, resource, objects);
        this.context=context;
        this.data=objects;
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        //create inflator
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflator.inflate(R.layout.custome_weather, parent, false);

        TextView city = (TextView) rowView.findViewById(R.id.city);
        ImageView weather=(ImageView)rowView.findViewById(R.id.wImage);
        TextView temerature=(TextView)rowView.findViewById(R.id.temperature);
        TextView desc=(TextView)rowView.findViewById(R.id.description);


        city.setText(data.get(position).getCity());
        weather.setImageBitmap(data.get(position).getImage());
        temerature.setText(data.get(position).gettemp());
        desc.setText(data.get(position).getDesc());

        return rowView;



    }

    @Override
    public void remove(WeatherItems object) {
        // TODO Auto-generated method stub


        data.remove(object);
        notifyDataSetChanged();
    }





    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }




}
