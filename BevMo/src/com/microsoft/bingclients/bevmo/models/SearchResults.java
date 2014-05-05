package com.microsoft.bingclients.bevmo.models;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SearchResults {
	
	private String mQuery;
	
	private String mTime;
	
	private String mIndex;
	
	private int mPosition;
	
	private int mCount;
	
	private String mError;
	
	private String mDescription;
	
	private ArrayList<SearchItem> mItems;
	
	public SearchResults() { 
		mItems = new ArrayList<SearchItem>(); 
	}
	
	public void setQuery(String query) { 
		mQuery = query; 
	}
	
	public String getQuery() { 
		return mQuery; 
	}

	public void setTime(String time) { 
		mTime = time; 
	}
	
	public String getTime() { 
		return mTime; 
	}
	
	public void setIndex(String index) { 
		mIndex = index; 
	}
	
	public String getIndex() { 
		return mIndex; 
	}
	
	public void setPosition(int position) { 
		mPosition = position; 
	}
	
	public int getPosition() { 
		return mPosition; 
	}
	
	public void setCount(int count) { 
		mCount = count; 
	}
	
	public int getCount() { 
		return mCount; 
	}
	
	public void setError(String error) { 
		mError = error; 
	}
	
	public String getError() { 
		return mError; 
	}
	
	public void setDescription(String description) { 
		mDescription = description; 
	}
	
	public String getDescription() { 
		return mDescription; 
	}
	
	public void addItem(SearchItem item) { 
		mItems.add(item); 
	}
	
	public ArrayList<SearchItem> getItems() { 
		return mItems;
	}
	
	public static SearchResults parseSearchResults(XmlPullParser parser) throws XmlPullParserException, IOException {
        SearchResults results = new SearchResults();
	    
	    while (parser.next() != XmlPullParser.END_DOCUMENT) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String prefix = parser.getPrefix();
	        String name = parser.getName();
	        
	        if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "q".equals(name)) {
	        	parser.next();
        		results.setQuery(parser.getText());
	        } else if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "index".equals(name)) {
	        	parser.next();
        		results.setIndex(parser.getText());
	        } else if (Constant.NAMESPACE_MAVISXQ.equals(prefix) && "error".equals(name)) {
	        	parser.next();
        		results.setError(parser.getText());
	        } else if ("description".equals(name)) {
	        	parser.next();
        		results.setDescription(parser.getText());
	        } else if ("pubDate".equals(name)) {
	        	parser.next();
        		results.setTime(parser.getText());
	        } else if ("item".equals(name)) {
	        	results.addItem(SearchItem.parseSearchItem(parser));
	        }
	    }
	    
	    return results;
	}
}
