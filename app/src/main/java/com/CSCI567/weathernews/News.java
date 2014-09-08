/*
 * 
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
import org.json.JSONStringer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class News extends Fragment {
	

	private View newsView;
	private myCustomNewsAdapter news_adapter;
	private ProgressDialog wProgressDialog;
	private URI news_uri;
	private HttpClient httpclient;
	private HttpResponse httpResponse ;
	private HttpEntity httpEntity;
	private String news_url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20feed%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	private ListView newsList;
	 private ArrayList<NewsItems> results = new ArrayList<NewsItems>();
	 private ArrayList<NewsItems> s;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		newsView=inflater.inflate(R.layout.fragment_news, container, false);
		newsList=(ListView)newsView.findViewById(R.id.newslistView);
		try{
		
			newsList.setOnItemClickListener(new OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					String url= results.get(arg2).getURL();
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
					startActivity(intent);

					
				}
				
			});
		
		
		}catch(Exception e)
		{
			Log.d("GJ", e.toString());
		}

		try{
			new JsonnewsDownload().execute();
		}catch(Exception e)
		{
			Log.d("News", e.toString());
		}
		return newsView;
	}
	
	

	
	
	private class JsonnewsDownload extends AsyncTask<Void,Void,Void> 
    {
		
		
        InputStream inputStream=null;
        String result = "";
   
      
        @Override
        protected void onPreExecute() {
        super.onPreExecute();
        
            wProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            wProgressDialog.setTitle("Fetching News...");
            // Set progressdialog message
            wProgressDialog.setMessage("Loading...");
            // Show progressdialog
            wProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            
        
            try {
                 news_uri = new URI(news_url);
				 httpclient = new DefaultHttpClient();
                 httpResponse = httpclient.execute(new HttpGet(news_uri));
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
                JSONObject Jarray=new JSONObject(result);
                
         
                	JSONObject json_query = Jarray.getJSONObject("query");
                    JSONObject json_results = json_query.getJSONObject("results");
                    JSONArray json_news = json_results.getJSONArray("item");
                    for (int i=0;i<json_news.length();i++){
                        JSONObject c = json_news.getJSONObject(i);
                        //Create e=new objects of News                        
                       results.add(new NewsItems(c.getString("title"),c.getString("link"),c.getString("pubDate")));
                
                    }
                
            }catch (Exception e)
            {
            	Log.e("Null Pointer Exeption","Error"+e.toString());
            }

        
            return null;
        }
        
        @Override
        protected void onPostExecute(Void args) {
        	s=results;
        	try{
        		news_adapter=new myCustomNewsAdapter(getActivity(),R.layout.custome_news, s);
            // Binds the Adapter to the ListView
            newsList.setAdapter(news_adapter);
            }catch(Exception e)
        	{
        		Log.d("GJ", "Error"+e.toString());
        	}
            // Close the progressdialog
        	wProgressDialog.dismiss();
        }

        
    }
        
	
	
	
	
	

}
