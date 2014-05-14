package com.microsoft.bingclients.eduapp.models;

public class SearchQuery {
	
	private String mText;
	
	private String mIndex;
	
	public SearchQuery() {
	}
	
	public void setText(String text) {
		text = text.trim().replace(" ", "%20");
		mText = text;
	}
	
	public String getText() { 
		return mText; 
	}
	
	public void setIndex(String index) { 
		mIndex = index; 
	}
	
	public String getIndex() { 
		return mIndex; 
	}
}
