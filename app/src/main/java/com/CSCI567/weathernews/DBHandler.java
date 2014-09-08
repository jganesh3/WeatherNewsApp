/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class DBHandler extends SQLiteOpenHelper {

    final static String DB_NAME = "WeatherNewsStock.db";
    final static int DB_VERSION = 1;
    private final String EXAMPLE_TABLE = "stock";
    private final String WEATHER_TABLE = "weather";
    private boolean isMultirecord = false;
    private boolean isMultilocation = false;


    public DBHandler(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (stockSymbol VARCHAR PRIMARY KEY);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WEATHER_TABLE + " (location VARCHAR PRIMARY KEY);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public boolean getmultirecordFlag() {
        return isMultirecord;
    }

    public boolean getmultilocationFlag() {
        return isMultilocation;
    }

    /*
     * Insert weather location
    */
    public boolean insertweatherLocation(String text) {
        try {
            SQLiteDatabase qdb = this.getWritableDatabase();
            Cursor c = qdb.rawQuery("SELECT * FROM " +
                    WEATHER_TABLE, null);

            if (c.getCount() > 1)
                isMultilocation = false;
            else
                isMultilocation = true;

            //Log.d("DB Insert: ", "INSERT OR REPLACE INTO " +
              //      WEATHER_TABLE + " (text) Values (" + text + ");");
            qdb.execSQL("INSERT OR REPLACE INTO " +
                    WEATHER_TABLE + "(location) Values (\"" + text + "\");");
            qdb.close();
        } catch (SQLiteException se) {
            Log.d("DB Insert Error: ", se.toString());
            return false;
        }
        return true;
    }

	/*
	*/

    public boolean insertText(String text) {
        try {

            SQLiteDatabase qdb = this.getWritableDatabase();
            Cursor c = qdb.rawQuery("SELECT * FROM " +
                    EXAMPLE_TABLE, null);

            if (c.getCount() > 1)
                isMultirecord = false;
            else
                isMultirecord = true;

            //Log.d("DB Insert: ", "INSERT OR REPLACE INTO " +
              //      EXAMPLE_TABLE + " (text) Values (" + text + ");");
            qdb.execSQL("INSERT OR REPLACE INTO " +
                    EXAMPLE_TABLE + "(stockSymbol) Values (\"" + text + "\");");
            qdb.close();
        } catch (SQLiteException se) {
            Log.d("DB Insert Error: ", se.toString());
            return false;
        }
        return true;
    }

    public String getText() {
        String toReturn = "";
        boolean flag = true;
        Vector<String> mystockSymbols = new Vector<String>();
        try {
            Log.d("Trace", "Inside get text");
            //DBHelper appDB = new DBHelper(context);
            SQLiteDatabase qdb = this.getReadableDatabase();
            qdb.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (stockSymbol VARCHAR);");
            Cursor crsr = qdb.rawQuery("SELECT * FROM " +
                    EXAMPLE_TABLE, null);

            if (crsr.getCount() > 1) {
                isMultirecord = false;
                if (crsr != null) {
                    if (crsr.moveToFirst()) {
                        do {
                            String text = crsr.getString(crsr.getColumnIndex("stockSymbol"));
                            mystockSymbols.add(text);

                        }
                        while (crsr.moveToNext());
                    }


                }
            } else {
                isMultirecord = true;
                if (crsr != null) {
                    if (crsr.moveToFirst()) {
                        do {
                            String text = crsr.getString(crsr.getColumnIndex("stockSymbol"));
                            mystockSymbols.add(text);

                        }
                        while (crsr.moveToNext());
                    }
                }
            }
            if (crsr == null) {
                flag = false;
                Log.d("Trace", "Unable to open cursor");
                Log.d("Trace", toReturn);
            }
            qdb.close();


        } catch (SQLiteException se) {
            Log.d("DB Select Error: ", se.toString());
            return "";
        }


        if (flag) {
            int i = 0;
            int lastSymbol = mystockSymbols.size();
            try {
                do {
                    toReturn += mystockSymbols.elementAt(i);
                    if (i != lastSymbol - 1)
                        toReturn += "%2C";
                    i++;

                } while (i < mystockSymbols.size());
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return toReturn;
    }
	
	
	/*
	 * Function which will return ArrayList
	*/


    public ArrayList<String> getallLocations() {
        ArrayList<String> data = new ArrayList<String>();
        try {
            Log.d("Trace", "Inside get text");

            SQLiteDatabase qdb = this.getReadableDatabase();
            Cursor crsr = qdb.rawQuery("SELECT * FROM " +
                    WEATHER_TABLE, null);

            if (crsr != null) {
                if (crsr.moveToFirst()) {
                    do {
                        String text = crsr.getString(crsr.getColumnIndex("location"));
                        data.add(text);

                    }
                    while (crsr.moveToNext());
                }
            } else
                data.add("");
        } catch (Exception e) {
            // TODO: handle exception
        }

        return data;


    }


    /*
     * Function which will return Favorite weather locations
    */
    public String getLocation() {
        String toReturn = "";
        boolean flag = true;
        Vector<String> myFavLocations = new Vector<String>();
        try {
            Log.d("Trace", "Inside get text");
            //DBHelper appDB = new DBHelper(context);
            SQLiteDatabase qdb = this.getReadableDatabase();
            //qdb.execSQL("CREATE TABLE IF NOT EXISTS " + WEATHER_TABLE + " (location VARCHAR);");
            Cursor crsr = qdb.rawQuery("SELECT * FROM " +
                    WEATHER_TABLE, null);

            if (crsr == null) {
                flag = false;
                Log.d("Trace", "Unable to open cursor");
                Log.d("Trace", toReturn);
                return "Errror";
            }

            if (crsr.getCount() > 1) {
                isMultilocation = true;
                if (crsr != null) {
                    if (crsr.moveToFirst()) {
                        do {
                            String text = crsr.getString(crsr.getColumnIndex("location"));
                            myFavLocations.add(text);

                        }
                        while (crsr.moveToNext());
                    }


                }
            } else {
                isMultilocation = false;
                if (crsr != null) {
                    if (crsr.moveToFirst()) {
                        do {
                            String text = crsr.getString(crsr.getColumnIndex("location"));
                            myFavLocations.add(text);

                        }
                        while (crsr.moveToNext());
                    }
                }
            }

            qdb.close();


        } catch (SQLiteException se) {
            Log.d("DB Select Error: ", se.toString());
            return "";
        }


        //for(int i=0;i<mystockSymbols.size();i++)

        if (flag) {
            int i = 0;
            int lastSymbol = myFavLocations.size();
            try {
                do {
                    toReturn += myFavLocations.elementAt(i);
                    if (i != lastSymbol - 1)
                        toReturn += "%2C";
                    i++;

                } while (i < myFavLocations.size());
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return toReturn;
    }


    /*
     *  End
    */
    public void deleteRow(String symbol) {
        try {
            SQLiteDatabase qdb = this.getWritableDatabase();
             String where = "value1 = 'string1'";
            String whereArgs[] = {symbol};
            qdb.delete(EXAMPLE_TABLE, new String("stockSymbol=?"), whereArgs);
            qdb.close();
        } catch (SQLiteException se) {
            Log.d("DB Insert Error: ", se.toString());

        }


    }
	
	/*
	 * Delete location
	*/

    public void deleteLocation(String location) {
        SQLiteDatabase qdb;
        try {
            qdb = this.getWritableDatabase();
            String where = "value1 = 'string1'";
            String whereArgs[] = {location};
            qdb.delete(WEATHER_TABLE, new String("location=?"), whereArgs);
            qdb.close();
        } catch (SQLiteException be) {
            Log.d("DB Insert Error: ", be.toString());
        }
    }

    /*
      end
    */
    public int getRowCount() {

        int rowCount = 0;
        try {
            //Log.d("Trace", "Inside get text");
            SQLiteDatabase qdb = this.getReadableDatabase();
            Cursor c = qdb.rawQuery("SELECT * FROM " + EXAMPLE_TABLE, null);
            if (c == null)
                Log.d("Trace", "Unable to open cursor");
            qdb.close();
            return c.getCount();


        } catch (SQLiteException se) {
            Log.d("DB Select Error: ", se.toString());

        }

        return rowCount;

    }

}
