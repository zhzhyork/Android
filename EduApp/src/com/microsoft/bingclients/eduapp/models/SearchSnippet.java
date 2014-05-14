package com.microsoft.bingclients.eduapp.models;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchSnippet implements Parcelable {
	
	private String mTime;
	
	private String mSpan;
	
	private ArrayList<SearchHit> mHits;
	
	public SearchSnippet() { 
		mHits = new ArrayList<SearchHit>(); 
	}
	
	public static final Parcelable.Creator<SearchSnippet> CREATOR
			= new Parcelable.Creator<SearchSnippet>() {
		public SearchSnippet createFromParcel(Parcel in) {
			return new SearchSnippet(in);
		}
		
		public SearchSnippet[] newArray(int size) {
			return new SearchSnippet[size];
		}
	};

	@SuppressWarnings("unchecked")
	private SearchSnippet(Parcel in) {
		mTime = in.readString();
		mSpan = in.readString();
		mHits = new ArrayList<SearchHit>();
		mHits = in.readArrayList(SearchHit.class.getClassLoader());
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mTime);
		dest.writeString(mSpan);
		dest.writeList(mHits);
	}
}
