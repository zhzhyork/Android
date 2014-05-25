package com.microsoft.bingclients.eduapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.AssetManager;

import com.microsoft.bingclients.eduapp.models.Course;

public class JsonParser {

	public static ArrayList<Course> parseCourses(AssetManager assetManager) {
		InputStream inputStream = null;
		
		try {  
			inputStream = assetManager.open("courses.json");  
        } catch (IOException e) {  
            e.printStackTrace();
        }  

		String json = readTextFile(inputStream);
		ArrayList<Course> courses = new ArrayList<Course>();
		
		try {
			JSONObject root = new JSONObject(json);
			JSONArray jsonArray = root.getJSONArray("courses");

		    for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        Course course = new Course();
		        course.setName(jsonObject.optString("Name"));
		        course.setTitle(jsonObject.optString("Title"));
		        course.addKey("Instructor");
		        course.addValue(jsonObject.optString("Instructor"));
		        course.addKey("Lab TA");
		        course.addValue(jsonObject.optString("Lab TA"));
		        course.addKey("Syllabus Online");
		        course.addValue(jsonObject.optString("Syllabus Online"));
		        course.addKey("Course Objective");
		        course.addValue(jsonObject.optString("Course Objective"));
		        course.addKey("Prerequisites");
		        course.addValue(jsonObject.optString("Prerequisites"));
		        course.addKey("Class Policy");
		        course.addValue(jsonObject.optString("Class Policy"));
		        course.addKey("Book");
		        course.addValue(jsonObject.optString("Book"));
		        course.addKey("Course Contents");
		        course.addValue(jsonObject.optString("Course Contents"));
		        course.addKey("Examination");
		        course.addValue(jsonObject.optString("Examination"));
		        
		        JSONArray classes = jsonObject.optJSONArray("Class");
		        if (classes != null) {
		        	for (int j = 0; j < classes.length(); j++) {
		        		course.addClass(classes.optString(j));
			        }
		        }
		        
		        JSONArray words = jsonObject.optJSONArray("Key Words");
		        if (words != null) {
		        	for (int k = 0; k < words.length(); k++) {
		        		course.addWord(words.optString(k));
			        }
		        }

		        courses.add(course);
		    }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

		return courses;
	}
	
	public static ArrayList<Course> generateTableFromCourses(ArrayList<Course> courses) {
		ArrayList<Course> table = new ArrayList<Course>();
		boolean added;
		
		for (int i = 0; i < 5; i ++) {
			for (int j = 0; j < 8; j ++) {
				int position = i * 8 + j;
				added = false;
				
				for (int m = 0; m < courses.size(); m ++) {
					ArrayList<String> classes = courses.get(m).getClassList();
					for (int n = 0; n < classes.size(); n ++) {
						String[] time = classes.get(n).split("-");
						int day = Integer.parseInt(time[0]);
						int hour = Integer.parseInt(time[1]);
						
						if (position == hour * 8 + day + 1) {
							table.add(courses.get(m));
							added = true;
							break;
						}
					}
				}
				
				if (!added) {
					table.add(new Course());
				}
			}
		}
		
		return table;
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
