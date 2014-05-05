package com.microsoft.bingclients.bevmo.models;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SearchHit {
	
	private int mCharPos;
	
	private int mNChars;
	
	public SearchHit() {
	}
	
	public void setCharPos(int charPos) { 
		mCharPos = charPos; 
	}
	
	public int getCharPos() { 
		return mCharPos; 
	}
	
	public void setNChars(int nChars) { 
		mNChars = nChars; 
	}
	
	public int getNChars() { 
		return mNChars; 
	}
	
	public static SearchHit parseSearchHit(XmlPullParser parser) throws XmlPullParserException, IOException {
		SearchHit hit = new SearchHit();
		
		hit.setCharPos(Integer.parseInt(parser.getAttributeValue(0)));
		hit.setNChars(Integer.parseInt(parser.getAttributeValue(1)));
	    
	    return hit;
	}
}
