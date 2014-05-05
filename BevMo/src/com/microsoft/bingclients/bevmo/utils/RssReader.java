package com.microsoft.bingclients.bevmo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.microsoft.bingclients.bevmo.models.Constant;
import com.microsoft.bingclients.bevmo.models.SearchQuery;
import com.microsoft.bingclients.bevmo.models.SearchResults;

public class RssReader {

	public static SearchResults read(SearchQuery query) {
		String source = String.format(Constant.MAVIS_URL, query.getIndex(), query.getText());
		InputStream in = getRssFeed(source);
		return parse(in);
	}
	
	private static InputStream getRssFeed(String source) {
	    InputStream in = null;
    
		try {
			URL url = new URL(source); 
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			in = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		return in;
	}
	
	private static SearchResults parse(InputStream in) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser parser = factory.newPullParser(); 
            parser.setInput(in, null);
            parser.nextTag();
            return SearchResults.parseSearchResults(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return null;
	}
}
