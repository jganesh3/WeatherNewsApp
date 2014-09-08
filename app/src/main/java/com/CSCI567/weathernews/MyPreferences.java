/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;


import java.util.ArrayList;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyPreferences extends Activity {

    EditText location;
    Button addLocation;
    DBHandler dbx;
    ArrayList<String> mylocations=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView list;



    @Override
    public void onCreate(Bundle Bundle) {
        super.onCreate(Bundle);
        setContentView(R.layout.activity_my_preferences);
        addLocation=(Button)findViewById(R.id.addlocation);
        location=(EditText)findViewById(R.id.locationET);
        dbx=new DBHandler(this, null, null, 0);
        list=(ListView)findViewById(R.id.locationListView);

        addLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbx.insertweatherLocation(location.getText().toString());
                mylocations.add(location.getText().toString());
                location.setText("");
                adapter.notifyDataSetChanged();


            }
        });





        try{
            list.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    removeItemfromList(arg2);
                    return false;
                }
            });
        }catch(Exception e)
        {
            Log.d("GJ", e.toString());
        }



        new UpdateLocation().execute();



    }




    private void removeItemfromList(int position)
    {
        try{

            final int deletePosition=position;
            String temp=mylocations.get(deletePosition);
            mylocations.remove(deletePosition);
            dbx.deleteLocation(temp);
            Toast.makeText(this,"Location "+temp+"deleted from favourite list!!", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();


        }catch(Exception e)
        {
            Log.d("GJ",	e.toString());
        }
    }






    private class UpdateLocation extends AsyncTask<Void,Void,Void> //implements OnItemClickListener
    {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            mylocations=dbx.getallLocations();
            return null;
        }





        @Override
        protected void onPostExecute(Void args) {


            adapter=new ArrayAdapter(getApplication(),android.R.layout.simple_list_item_1 , mylocations);
            list.setAdapter(adapter);

        }

    }






}
