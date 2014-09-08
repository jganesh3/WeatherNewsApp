package weather.and.newsapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbutil.Dbutil;

/**
 * Created by GJ on 9/7/2014.
 */
public class Stocks extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String stockName=(String)req.getParameter("stockname");
        String[] stockArray=stockName.split(",");
        StringBuilder str=new StringBuilder();
        for( int i = 0 ; i < stockArray.length; i++ ) {
            str.append("?");
            if(i<stockArray.length-1)
                str.append(" , ");

        }

        PrintWriter out=resp.getWriter();
        JSONArray stocklist=new JSONArray();
        JSONObject stock=null;
        Connection connection = null;
        ResultSet rs;
        PreparedStatement preStmt = null;


        try{
            connection = Dbutil.getConnection();
            String sql="select symbol,name,day_high,day_low, changeValue from stocks where symbol in ( "+str+" )";
            preStmt = connection.prepareStatement(sql);
            for(int j=0;j<stockArray.length;j++)
                preStmt.setString(j+1, stockArray[j]);

            rs = preStmt.executeQuery();

            while(rs.next()){

                stock=new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                stock.put("symbol",rs.getString(1));
                stock.put("name",rs.getString(2));
                stock.put("day_high",rs.getString(3));
                stock.put("day_low",rs.getString(4));
                stock.put("changeValue",rs.getString(5));
                stocklist.put(stock);



            }

        }catch (SQLException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        resp.setContentType("application/json");
        System.out.println(stocklist.toString());
        //return weather.toString();
        out.print(stocklist.toString());


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
