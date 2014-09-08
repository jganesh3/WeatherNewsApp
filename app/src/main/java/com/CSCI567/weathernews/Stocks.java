/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class Stocks extends Fragment {

    private View stockView;
    private JsonStockDownload stockdata;
    private boolean multiRecords;
    private EditText symbol;
    private Button addtoList,removeFromList;
    private ListView stocklist;
    private String [] items = {"No Suggestions"};
    private ProgressDialog mProgressDialog;
    private ListView listView1 ;
    //ArrayAdapter<String> adapter;
    private ArrayAdapter<StocksItems> adapter;
    private URI uri;
    private HttpClient httpclient;
    private HttpResponse httpResponse ;
    private InputStream inputStream;
    private HttpEntity httpEntity;
    private String url;
    private DBHandler dbh;


    public Stocks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //Log.d("Trace GJ", "GJ");
        updateURL();
        multiRecords=dbh.getmultirecordFlag();
        new JsonStockDownload().execute();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub

        inflater.inflate(R.menu.main_activity_action, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        stockView=inflater.inflate(R.layout.fragment_stocks, container, false);
        dbh=new DBHandler(getActivity(), null, null, 0);
        symbol=(EditText)stockView.findViewById(R.id.stockSymbol);
        stocklist=(ListView)stockView.findViewById(R.id.listView);
        addtoList=(Button)stockView.findViewById(R.id.addToList);
        addtoList.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateFavourites();
                updateURL();
                multiRecords=dbh.getmultirecordFlag();
                new JsonStockDownload().execute();

            }
        });


        updateURL();
        multiRecords=dbh.getmultirecordFlag();
        new JsonStockDownload().execute();

        return stockView;
    }

    // This function will update the Data Base
    private void updateFavourites()
    {

        dbh.insertText(symbol.getText().toString());
        symbol.setText("");

    }

    private void updateURL(){

        String favouriteSymbol=dbh.getText();
        //url="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20("+ favouriteSymbol+")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        url="http://192.168.0.105:8080/WeatherandNewsService/stocks?stockname="+favouriteSymbol;
    }



    private class JsonStockDownload extends AsyncTask<Void,Void,Void> //implements OnItemClickListener
    {


        InputStream inputStream=null;
        String result = "";
        ArrayList<StocksItems> results = new ArrayList<StocksItems>();
        ArrayList<StocksItems> s;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Fetching Stocks...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                uri = new URI(url);
                httpclient = new DefaultHttpClient();
                httpResponse = httpclient.execute(new HttpGet(uri));
                httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            } catch (URISyntaxException e) {
                Log.e("URISyntaxException ", e.toString());
                e.printStackTrace();
            }

            try {

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }
                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }


            try{
                JSONArray Jarray=new JSONArray(result);
                JSONObject jsobject;

                for(int i=0;i<Jarray.length();i++){

                    jsobject=Jarray.getJSONObject(i);
                    String symbol=jsobject.getString("symbol");
                    String name=jsobject.getString("name");
                    String day_high=jsobject.getString("day_high");
                    String day_low=jsobject.getString("day_low");
                    String changeValue=jsobject.getString("changeValue");

                    results.add(new StocksItems(symbol, name, day_high,day_low, changeValue));




                }

              /*  if(multiRecords)
                {
                JSONObject json_query = Jarray.getJSONObject("query");
                JSONObject json_results = json_query.getJSONObject("results");
                JSONObject json_json_result = json_results.getJSONObject("quote");
                
                 results.add(new StocksItems(json_json_result.getString("Name"), json_json_result.getString("symbol"), json_json_result.getString("DaysHigh"), json_json_result.getString("DaysLow"), json_json_result.getString("Change")));
                }else
                {
         
                    JSONObject json_query = Jarray.getJSONObject("query");
                    JSONObject json_json_obj = json_query.getJSONObject("results");
                    JSONArray json_json_array = json_json_obj.getJSONArray("quote");
                    
                    for (int i=0;i<json_json_array.length();i++){
                        JSONObject c = json_json_array.getJSONObject(i);
                        
                       results.add(new StocksItems(c.getString("Name"), c.getString("symbol"), c.getString("DaysHigh"), c.getString("DaysLow"), c.getString("Change")));
                        
                        

                    }

                	
                }*/
            }catch (Exception e)
            {
                Log.e("Null Pointer Exeption","Error"+e.toString());
            }


            return null;
        }




        private void removeItemfromList(int position)
        {
            try{
                final int deletePosition=position;
                StocksItems temp=s.get(deletePosition);
                s.remove(deletePosition);
                dbh.deleteRow(temp.getSymbol());
                Toast.makeText(getActivity(),temp.getSymbol()+" Stock deleted from favourite list!!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }catch(Exception e)
            {
                Log.d("GJ",	e.toString());
            }
        }

        @Override
        protected void onPostExecute(Void args) {
            s=results;
            adapter=new myCustomStockAdapter(getActivity(), R.layout.custom_stocks,s);

            // Binds the Adapter to the ListView
            stocklist.setAdapter(adapter);
            // stocklist.setLongClickable(true);
            //stocklist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            try{
                stocklist.setOnItemLongClickListener(new OnItemLongClickListener() {

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



            // Close the progressdialog
            mProgressDialog.dismiss();
        }



    }




}
