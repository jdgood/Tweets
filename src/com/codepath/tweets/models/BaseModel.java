package com.codepath.tweets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseModel {
	protected JSONObject jsonOject;

	public String getJSONString() {
		return jsonOject.toString();
	}
	
	protected String getString(String name) {
		try {
			return jsonOject.getString(name);
		}
		catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected long getLong(String name) {
		try {
			return jsonOject.getLong(name);
		}
		catch(JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	protected int getInt(String name) {
		try {
			return jsonOject.getInt(name);
		}
		catch(JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	protected double getDouble(String name) {
		try {
			return jsonOject.getDouble(name);
		}
		catch(JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	protected boolean getBoolean(String name) {
		try {
			return jsonOject.getBoolean(name);
		}
		catch(JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}
