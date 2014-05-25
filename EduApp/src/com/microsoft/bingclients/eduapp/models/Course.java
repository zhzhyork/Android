package com.microsoft.bingclients.eduapp.models;

import java.util.ArrayList;

public class Course {

	private String mName;
	
	private String mTitle;
	
	private ArrayList<String> mClass;
	
	private ArrayList<String> mWords;
	
	private ArrayList<String> mKeys; 
	
	private ArrayList<String> mValues; 
	
	public Course() {
		mClass = new ArrayList<String>();
		mWords = new ArrayList<String>();
		mKeys = new ArrayList<String>();
		mValues = new ArrayList<String>();
	}
	
	public void setName(String name) { 
		mName = name; 
	}
	
	public String getName() { 
		return mName; 
	}
	
	public void setTitle(String title) { 
		mTitle = title; 
	}
	
	public String getTitle() { 
		return mTitle; 
	}
	
	public void addClass(String classname) { 
		mClass.add(classname);
	}
	
	public ArrayList<String> getClassList() { 
		return mClass; 
	}
	
	public void addWord(String word) { 
		mWords.add(word);
	}
	
	public ArrayList<String> getWords() { 
		return mWords; 
	}

	public void addKey(String key) { 
		mKeys.add(key);
	}
	
	public ArrayList<String> getKeys() { 
		return mKeys; 
	}
	
	public void addValue(String value) { 
		mValues.add(value);
	}
	
	public ArrayList<String> getValues() { 
		return mValues; 
	}
}
