/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;

public class Weather extends Fragment implements MultiChoiceModeListener {

    private View weatherView;
    private ListView weatherList;
    private ProgressDialog wProgressDialog;
    private myCustomweatherAdapter weather_adapter;
    private ArrayList<WeatherItems> shareItems = new ArrayList<WeatherItems>();
    private URI wether_uri;
    private HttpClient httpclient;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;
    private String weather_url;
    private final String imageUrl = "http://l.yimg.com/a/i/us/we/52/";
    private Bitmap mIcon11 = null;
    private ArrayList<WeatherItems> s;
    MenuItem menuSearch;
    EditText editsearch;
    private boolean isMultiLocation;
    DBHandler dbW;
    private ShareActionProvider myShareActionProvider;
    private ArrayList<WeatherItems> results = new ArrayList<WeatherItems>();
    private String share = "";

    public Weather() {


    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        Log.d("GJ", "onResume ............");
        super.onResume();
    }


    private void updateWURL() {
        String favouriteLocation = dbW.getLocation();
        //weather_url="https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20weather.bylocation%20WHERE%20location%20in("+favouriteLocation+")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        weather_url = "http://192.168.0.105:8080/WeatherandNewsService/weather?city=" + favouriteLocation;
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

        if (item.getItemId() == R.id.settings) {
            //Log.d("Trace GJ", "GJ");
            Intent i = new Intent();
            i.setClass(getActivity(), MyPreferences.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.refresh_icon) {
            updateWURL();
            isMultiLocation = dbW.getmultilocationFlag();
            results.clear();
            new JsonweatherDownload().execute();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /** Inflating the current activity's menu with res/menu/items.xml */
        inflater.inflate(R.menu.wether, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Weather");
        for (int i = 0; i < shareItems.size(); i++) {
            share = "City:" + shareItems.get(i).getCity() + "\n" + shareItems.get(i).gettemp() + " F" + "\n" + shareItems.get(i).getDesc() + "\n";
        }
        intent.putExtra(Intent.EXTRA_TEXT, share);
        return intent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            weatherView = inflater.inflate(R.layout.fragment_weather, container, false);
            dbW = new DBHandler(getActivity(), null, null, 0);
            weatherList = (ListView) weatherView.findViewById(R.id.weatherlistView);
            updateWURL();
            weatherList.setMultiChoiceModeListener(this);
            weatherList.setChoiceMode(weatherList.CHOICE_MODE_MULTIPLE_MODAL);
            isMultiLocation = dbW.getmultilocationFlag();
            new JsonweatherDownload().execute();
        } catch (Exception e) {
            Log.d("GJ", e.toString());
        }
        return weatherView;

    }

	
	
	/*
	 * Async Task to fetch JSON 
	*/

    private class JsonweatherDownload extends AsyncTask<Void, Void, Void> {


        InputStream inputStream = null;
        String result = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            wProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            wProgressDialog.setTitle("Fetching Weather...");
            // Set progressdialog message
            wProgressDialog.setMessage("Loading...");
            // mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            wProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                wether_uri = new URI(weather_url);
                httpclient = new DefaultHttpClient();
                httpResponse = httpclient.execute(new HttpGet(wether_uri));
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


            try {
                //JSONObject Jarray=new JSONObject(result);
                JSONArray Jarray = new JSONArray(result);
                for (int j = 0; j < Jarray.length(); j++) {
                    JSONObject json_obj = Jarray.getJSONObject(j);
                    JSONObject location = json_obj.getJSONObject("location");
                    String last_buildDate = json_obj.getString("lastBuildDate");
                    JSONObject item = json_obj.getJSONObject("item");
                    JSONObject condition = item.getJSONObject("condition");
                    String wcode = condition.getString("code");

                    try {
                        InputStream in = new java.net.URL(imageUrl+wcode+".gif").openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    results.add(new WeatherItems(location.getString("city"), mIcon11, condition.getString("tempinF"), last_buildDate, condition.getString("text")));


                }
            } catch (Exception e) {
                Log.e("Null Pointer Exeption", "Error" + e.toString());
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            s = results;
            try {
                weather_adapter = new myCustomweatherAdapter(getActivity(), R.layout.custome_weather, s);
                // Binds the Adapter to the ListView
                weatherList.setAdapter(weather_adapter);
            } catch (Exception e) {
                Log.d("GJ", "Error" + e.toString());
            }
            // Close the progressdialog
            wProgressDialog.dismiss();
        }


    }


    @Override
    public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
        // TODO Auto-generated method stub
        arg0.getMenuInflater().inflate(R.menu.delete, arg1);

        myShareActionProvider = (ShareActionProvider) arg1.findItem(R.id.menu_share).getActionProvider();


        return true;
    }


    @Override
    public void onDestroyActionMode(ActionMode arg0) {
        // TODO Auto-generated method stub
        Log.d("GJ", "Inside on destroyMenu");
        share = "";
        shareItems.clear();

    }


    @Override
    public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2,
                                          boolean arg3) {
        // TODO Auto-generated method stub

        final int checkedCount = weatherList.getCheckedItemCount();
        Log.d("GJ", "The selected position is" + arg1);
        WeatherItems tmp = results.get(arg1);
        shareItems.add(tmp);
        arg0.setTitle(checkedCount + " Selected");
        /** Setting a share intent */
        myShareActionProvider.setShareIntent(getDefaultShareIntent());


    }


}
