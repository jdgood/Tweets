package com.codepath.tweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "X0xD24gy64nhzJ9pUFk7g";       // Change this
    public static final String REST_CONSUMER_SECRET = "RbnO84p8qdj1Mo83Rc450imrfHYnpsM4qeQa0QoftZ0"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(long lastTweet, boolean pullNew, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	makeTimelineCall(lastTweet, pullNew, url, handler);
    }
    
    public void getMentionsTimeline(long lastTweet, boolean pullNew, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	makeTimelineCall(lastTweet, pullNew, url, handler);
    }
    
    public void getUserTimeline(long lastTweet, boolean pullNew, String user, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json");
    	
    	RequestParams params = new RequestParams();
    	
        if(lastTweet != 0) {
        	if(pullNew) {
        		params.put("since_id", Long.toString(lastTweet + 1));
        		params.put("count", "200");
        	}
        	else {
        		params.put("max_id", Long.toString(lastTweet - 1));
        		params.put("count", "25");
        	}
        }
        else {
        	params.put("count", "25");
        }
        
        if(user != null) {
        	params.put("screen_name", user);
        }
        
    	client.get(url, params, handler);
    }
    
    private void makeTimelineCall(long lastTweet, boolean pullNew, String url, AsyncHttpResponseHandler handler) {
    	RequestParams params = new RequestParams();
    	
        if(lastTweet != 0) {
        	if(pullNew) {
        		params.put("since_id", Long.toString(lastTweet + 1));
        		params.put("count", "200");
        	}
        	else {
        		params.put("max_id", Long.toString(lastTweet - 1));
        		params.put("count", "25");
        	}
        }
        else {
        	params.put("count", "25");
        }
    	client.get(url, params, handler);
    }
    
    public void getUserInfo(String user, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	
    	RequestParams params = null;
    	if(user != null) {
    		params = new RequestParams();
    		url = getApiUrl("users/show.json");
    		params.put("screen_name", user);
    	}
    	
    	client.get(url, params, handler);
    }
    
    public void sendTweet(String body, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
		params.put("status", body);
    	client.post(url, params, handler);
    }
}