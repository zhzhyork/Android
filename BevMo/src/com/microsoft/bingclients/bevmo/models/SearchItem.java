package com.microsoft.bingclients.bevmo.models;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SearchItem {
	
	private String mId;
	
	private String mTitle;
	
	private String mDescription;
	
	private String mImage;
	
	private String mDuration;
	
	private String mUrl;
	
	private String mOrigin;
	
	private String mOriginUrl;
	
	private String mPubDate;
	
	private ArrayList<SearchSnippet> mSnippets;
	
	public SearchItem() { 
		mSnippets = new ArrayList<SearchSnippet>(); 
	}

	public void setId(String id) { 
		mId = id; 
	}
	
	public String getId() { 
		return mId; 
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
	
	public void setImage(String image) { 
		mImage = image; 
	}
	
	public String getImage() { 
		return mImage; 
	}
	
	public void setDuration(String duration) { 
		mDuration = duration; 
	}
	
	public String getDuration() { 
		return mDuration; 
	}
	
	public void setUrl(String url) { 
		mUrl = url; 
	}
	
	public String getUrl() { 
		return mUrl; 
	}
	
	public void setOrigin(String origin) { 
		mOrigin = origin; 
	}
	
	public String getOrigin() { 
		return mOrigin; 
	}
	
	public void setOriginUrl(String originUrl) { 
		mOriginUrl = originUrl; 
	}
	
	public String getOriginUrl() { 
		return mOriginUrl; 
	}
	
	public void setPubDate(String pubDate) { 
		mPubDate = pubDate; 
	}
	
	public String getPubDate() { 
		return mPubDate; 
	}
	
	public void addSnippet(SearchSnippet snippet) { 
		mSnippets.add(snippet); 
	}
	
	public ArrayList<SearchSnippet> getSnippets() { 
		return mSnippets; 
	}
	
	public static SearchItem parseSearchItem(XmlPullParser parser) throws XmlPullParserException, IOException {
		SearchItem item = new SearchItem();
	    
	    while (parser.next() != XmlPullParser.END_TAG || !"item".equals(parser.getName())) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String prefix = parser.getPrefix();
	        String name = parser.getName();
	        
	        if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "id".equals(name)) {
	        	parser.next();
        		item.setId(parser.getText());
	        } else if ("title".equals(name)) {
	        	parser.next();
	        	item.setTitle(parser.getText());
	        } else if ("description".equals(name)) {
	        	parser.next();
        		item.setDescription(parser.getText());
	        } else if (Constant.NAMESPACE_ITUNES.equals(prefix) && "image".equals(name)) {
        		item.setImage(parser.getAttributeValue(0));
	        } else if (Constant.NAMESPACE_ITUNES.equals(prefix) && "duration".equals(name)) {
	        	parser.next();
        		item.setDuration(parser.getText());
	        } else if ("enclosure".equals(name)) {
        		item.setUrl(parser.getAttributeValue(0));
	        } else if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "origin".equals(name)) {
        		item.setOriginUrl(parser.getAttributeValue(0));
        		parser.next();
        		item.setOrigin(parser.getText());
	        } else if ("pubDate".equals(name)) {
	        	parser.next();
        		item.setPubDate(parser.getText());
	        } else if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "snippet".equals(name)) {
        		item.addSnippet(SearchSnippet.parseSearchSnippet(parser));
	        }
	    }
	    
	    return item;
	}
}
