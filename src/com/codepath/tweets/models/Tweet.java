package com.codepath.tweets.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tweet extends BaseModel {
	private User user;

	public User getUser() {
		return user;
	}
	
	public String getBody() {
		return getString("text");
	}
	
	public long getId() {
		return getLong("id");
	}
	
	public boolean isFavorited() {
		return getBoolean("favorited");
	}
	
	public boolean isTweeted() {
		return getBoolean("retweeted");
	}
	
	public String getTimestamp() {
		return getString("created_at");
	}
	
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.jsonOject = jsonObject;
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for(int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			
			try {
				tweetJson = jsonArray.getJSONObject(i);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if(tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}
}
