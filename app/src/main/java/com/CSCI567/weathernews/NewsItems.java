/*
 * Weather & News App.
 * CSCI 567 Project
 * Author : Ganesh Joshi
 * 
*/
package com.CSCI567.weathernews;

public class NewsItems {

	private String title;
	private String url;
	private String publishdate;
	
	
	 public NewsItems(String title,String url,String publishdate) {
		// TODO Auto-generated constructor stub
		
		 this.title=title;
		 this.url=url;
		 this.publishdate=publishdate;
		
	}
	 
	 
	 public String getTitle()
	 {
		 return this.title;
	 }
	 
	 public String getURL()
	 {
		 return this.url;
	 }
	 public String getDate()
	 {
		 return this.publishdate;
	 }
	 
}
