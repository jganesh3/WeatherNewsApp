/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

public class StocksItems {

    private String symbol;
    private String Name;
    private String DaysHigh;
    private String DaysLow;
    private String Change;


    public StocksItems(String Name,String symbol,String dHigh,String Dlow,String change) {
        // TODO Auto-generated constructor stub
        this.symbol=symbol;
        this.Name=Name;
        this.DaysHigh=dHigh;
        this.DaysLow=Dlow;
        this.Change=change;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public String getName(){
        return this.Name;
    }
    public String getDHigh(){
        return this.DaysHigh;
    }

    public String getDlow(){
        return this.DaysLow;
    }

    public String getChange(){
        return this.Change;
    }





}
