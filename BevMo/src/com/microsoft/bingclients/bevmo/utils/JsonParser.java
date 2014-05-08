package com.microsoft.bingclients.bevmo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.AssetManager;

import com.microsoft.bingclients.bevmo.models.Channel;

public class JsonParser {

	public static ArrayList<Channel> parseChannels(AssetManager assetManager) {
		InputStream inputStream = null;
		
		try {  
			inputStream = assetManager.open("channels.json");  
        } catch (IOException e) {  
            e.printStackTrace();
        }  

		String json = readTextFile(inputStream);
		ArrayList<Channel> channels = new ArrayList<Channel>();
		
		try {
			JSONObject root = new JSONObject(json);
			JSONArray jsonArray = root.getJSONArray("channels");

		    for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        Channel channel = new Channel();
		        channel.setIndex(jsonObject.optString("index"));
		        channel.setName(jsonObject.optString("name"));
		        channel.setTitle(jsonObject.optString("title"));
		        channel.setDescription(jsonObject.optString("description"));
		        channel.setUpdate(jsonObject.optString("update"));
		        
		        JSONArray recommendations = jsonObject.optJSONArray("recommendation");
		        if (recommendations != null) {
		        	for (int j = 0; j < recommendations.length(); j++) {
		        		JSONObject recommendation = recommendations.optJSONObject(j);
			        	channel.addRecommendation(recommendation.optString("title"), 
			        			recommendation.optString("image"), 
			        			recommendation.optString("duration"),
			        			recommendation.optString("url"));
			        }
		        }
		        
		        JSONArray powers = jsonObject.optJSONArray("power");
		        if (powers != null) {
		        	for (int k = 0; k < powers.length(); k++) {
			        	JSONObject power = powers.optJSONObject(k);
			        	channel.addPower(power.optString("name"), power.optString("link"));
			        }
		        }
		        
		        channels.add(channel);
		    }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

		return channels;
	}
	
	private static String readTextFile(InputStream inputStream) {  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
		byte buf[] = new byte[1024];  
		int len;
		
		try {  
			while ((len = inputStream.read(buf)) != -1) {  
				outputStream.write(buf, 0, len);  
			}
			
			outputStream.close();  
			inputStream.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toString();  
	}  
}
