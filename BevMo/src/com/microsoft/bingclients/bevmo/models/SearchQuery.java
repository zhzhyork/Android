package com.microsoft.bingclients.bevmo.models;

public class SearchQuery {
	
	private String mText;
	
	private String mIndex;
	
	public SearchQuery() {
	}
	
	public void setText(String text) { 
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
