/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

import android.graphics.Bitmap;

public class WeatherItems {

	private String city;
	private Bitmap image;
	private String temperature;
	private String date;
	private String weather_desciption; // sunny or couldy
	
	public WeatherItems(String city,Bitmap image, String temp,String Date,String desc) {
		// TODO Auto-generated constructor stub
		this.city=city;
		this.image=image;
		this.temperature=temp;
		this.date=date;
		this.weather_desciption=desc;
		
	}
	
	public String getCity(){
		return city;
	}
	
	public Bitmap getImage(){
		return this.image;
	}
	
	public String gettemp(){
		return this.temperature;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getDesc(){
		return this.weather_desciption;
	}
	

}
