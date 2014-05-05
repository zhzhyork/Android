package com.microsoft.bingclients.bevmo.models;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SearchSnippet {
	
	private String mTime;
	
	private String mSpan;
	
	private ArrayList<SearchHit> mHits;
	
	public SearchSnippet() { 
		mHits = new ArrayList<SearchHit>(); 
	}
	
	public void setTime(String time) {
		mTime = time;
	}
	
	public String getTime() {
		return mTime;
	}
	
	public void setSpan(String span) {
		mSpan = span;
	}
	
	public String getSpan() {
		return mSpan;
	}
	
	public void addHit(SearchHit hit) {
		mHits.add(hit);
	}
	
	public ArrayList<SearchHit> getHits() {
		return mHits;
	}
	
	public static SearchSnippet parseSearchSnippet(XmlPullParser parser) throws XmlPullParserException, IOException {
		SearchSnippet snippet = new SearchSnippet();
		
		snippet.setTime(parser.getAttributeValue(0));
	    
		while (parser.next() != XmlPullParser.END_TAG || !"snippet".equals(parser.getName())) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String prefix = parser.getPrefix();
	        String name = parser.getName();
	        
	        if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "span".equals(name)) {
	        	parser.next();
        		snippet.setSpan(parser.getText().replace('@', '.'));
	        } else if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "hit".equals(name)) {
	        	snippet.addHit(SearchHit.parseSearchHit(parser));
	        }
	    }
	    
	    return snippet;
	}
}
