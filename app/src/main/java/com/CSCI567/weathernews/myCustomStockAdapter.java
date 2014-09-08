/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class myCustomStockAdapter extends ArrayAdapter<StocksItems> {

    private Context context;
    private List<StocksItems> data;

    public myCustomStockAdapter(Context context, int resource, List<StocksItems> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        //create inflator
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflator.inflate(R.layout.custom_stocks, parent, false);

        TextView symbol = (TextView) rowView.findViewById(R.id.symbol);
        TextView Name = (TextView) rowView.findViewById(R.id.Name);
        TextView DaysHigh = (TextView) rowView.findViewById(R.id.daysHigh);
        TextView daysLow = (TextView) rowView.findViewById(R.id.daysLow);
        TextView change = (TextView) rowView.findViewById(R.id.change);

        symbol.setText(data.get(position).getSymbol());
        Name.setText(data.get(position).getName());
        DaysHigh.setText(data.get(position).getDHigh());
        daysLow.setText(data.get(position).getDlow());
        Name.setText(data.get(position).getName());
        change.setText(data.get(position).getChange());

        return rowView;
    }


}


