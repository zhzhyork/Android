package com.microsoft.bingclients.bevmo.models;

import java.util.ArrayList;

public class Channel {
	
	private String mIndex;
    
	private String mName;
	
	private String mTitle;
	
	private String mDescription;
	
	private ArrayList<String> mQuery;
	
	private ArrayList<Power> mPower;
	
	private String mUpdate;
	
	public Channel() {
		mQuery = new ArrayList<String>();
		mPower = new ArrayList<Power>();
	}
	
	public void setIndex(String index) { 
		mIndex = index; 
	}
	
	public String getIndex() { 
		return mIndex; 
	}
	
	public void setName(String name) { 
		mName = name; 
	}
	
	public String getName() { 
		return mName; 
	}
	
	public void setTitle(String title) { 
		mTitle = title; 
	}
	
	public String getTitle() { 
		return mTitle; 
	}
	
	public void setDescription(String description) { 
		mDescription = description; 
	}
	
	public String getDescription() { 
		return mDescription; 
	}
	
	public void setUpdate(String update) { 
		mUpdate = update; 
	}
	
	public String getUpdate() { 
		return mUpdate; 
	}
	
	public void addQuery(String query) {
		mQuery.add(query);
	}
	
	public ArrayList<String> getQuery() { 
		return mQuery;
	}
	
	public void addPower(String name, String link) {
		Power power = new Power();
		power.name = name;
		power.link = link;
		mPower.add(power);
	}
	
	public ArrayList<Power> getPower() { 
		return mPower; 
	}
	
	public class Power {
		
		public String name;
		
	    public String link;
	}
}
