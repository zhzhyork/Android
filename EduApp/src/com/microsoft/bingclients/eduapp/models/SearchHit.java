package com.microsoft.bingclients.eduapp.models;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchHit implements Parcelable {
	
	private int mCharPos;
	
	private int mNChars;
	
	public SearchHit() {
	}
	
	public static final Parcelable.Creator<SearchHit> CREATOR
			= new Parcelable.Creator<SearchHit>() {
		public SearchHit createFromParcel(Parcel in) {
			return new SearchHit(in);
		}
		
		public SearchHit[] newArray(int size) {
			return new SearchHit[size];
		}
	};

	private SearchHit(Parcel in) {
		mCharPos = in.readInt();
		mNChars = in.readInt();
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mCharPos);
		dest.writeInt(mNChars);
	}
}
