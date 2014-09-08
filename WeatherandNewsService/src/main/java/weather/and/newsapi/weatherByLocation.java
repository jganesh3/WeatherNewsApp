/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 *
*/
package weather.and.newsapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import dbutil.*;





public class weatherByLocation extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        /*resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
        */

        String cityNamerequest = (String) req.getParameter("city");
        System.out.println("Before String Replace  "+cityNamerequest);
        String []cityarray=cityNamerequest.split(",");
        StringBuilder str=new StringBuilder();
        for( int i = 0 ; i < cityarray.length; i++ ) {
            str.append("?");
            if(i<cityarray.length-1)
                str.append(" , ");

        }
        //String cityName=cityNamerequest.replace("%2C",",");
        PrintWriter out=resp.getWriter();
        JSONArray weatherObject=new JSONArray();
        JSONObject weather=null;
        JSONObject location = null;
        JSONObject item = null;
        JSONObject condition =null;
        Connection connection = null;
        ResultSet rs;
        PreparedStatement preStmt = null;
       // System.out.println("This is what is received after replace  "+cityName);

        try {
            connection = Dbutil.getConnection();
            //String sql="select cityName, country, region, temperatureinC, temperatureinF, weatherCode, weatherText from city left join weathercondition on (cityName=city) where cityName in ( ? )";
            String sql="select cityName, country, region, temperatureinC, temperatureinF, weatherCode,weatherText from weather c where c.cityName in ("+str+")";
            preStmt = connection.prepareStatement(sql);
            for(int j=0;j<cityarray.length;j++)
                preStmt.setString(j+1, cityarray[j]);

            rs = preStmt.executeQuery();
              while (rs.next()) {
                weather=new JSONObject();
                location=new JSONObject();
                item=new JSONObject();
                condition=new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                //if(rs.first()) {
                weather.put("Tittle", "my Wether REST API");
                weather.put("language", "en-us");
                weather.put("lastBuildDate", dateFormat.format(date));
                // Retrieve by column name
                String city = rs.getString("cityName");
                location.put("city", city);

                String country = rs.getString("country");
                location.put("country", country);

                String region = rs.getString("region");
                location.put("region", region);

                weather.put("location", location);
                item.put("title", "condition for " + city + " at "
                        + dateFormat.format(date));

                int weatherCode = rs.getInt("weatherCode");
                condition.put("code", weatherCode);
                int temperatureinC = rs.getInt("temperatureinC");
                condition.put("tempinC", temperatureinC);
                int temperatureinF = rs.getInt("temperatureinC");
                condition.put("tempinF", temperatureinF);
                String weatherText = rs.getString("weatherText");
                condition.put("text", weatherText);
                // add condition in item
                item.put("condition", condition);
                // add item in weather
                weather.put("item", item);

                weatherObject.put(weather);


            }

        } catch (SQLException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();
        }

        resp.setContentType("application/json");
        System.out.println(weatherObject.toString());
        //return weather.toString();
        out.print(weatherObject.toString());


    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    }
}