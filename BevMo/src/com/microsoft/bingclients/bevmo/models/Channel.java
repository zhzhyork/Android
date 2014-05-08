package com.microsoft.bingclients.bevmo.models;

import java.util.ArrayList;

public class Channel {
	
	private String mIndex;
    
	private String mName;
	
	private String mTitle;
	
	private String mDescription;
	
	private ArrayList<Video> mRecommendations;
	
	private ArrayList<Power> mPowers;
	
	private String mUpdate;
	
	public Channel() {
		mRecommendations = new ArrayList<Video>();
		mPowers = new ArrayList<Power>();
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
	
	public void addRecommendation(String title, String image, String duration, String url) {
		Video video = new Video();
		video.title = title;
		video.image = image;
		video.duration = duration;
		video.url = url;
		mRecommendations.add(video);
	}
	
	public ArrayList<Video> getRecommendations() { 
		return mRecommendations;
	}
	
	public void addPower(String name, String link) {
		Power power = new Power();
		power.name = name;
		power.link = link;
		mPowers.add(power);
	}
	
	public ArrayList<Power> getPowers() { 
		return mPowers; 
	}
	
	public class Power {
		
		public String name;
		
	    public String link;
	}
	
	public class Video {
		
		public String title;
		
		public String image;
		
		public String duration;
		
		public String url;
	}
}
