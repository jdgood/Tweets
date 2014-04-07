package com.codepath.tweets.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.codepath.tweets.MyTwitterApp;
import com.codepath.tweets.R;
import com.codepath.tweets.adapters.TweetAdapter;
import com.codepath.tweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MyTwitterApp.getRestClient().getMentionsTimeline(0, true, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				results = Tweet.fromJson(jsonTweets);
				
				ListView lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
				adapter = new TweetAdapter(getActivity(), results);
				lvTweets.setAdapter(adapter);
				if(results.size() > 0) {
					firstTweet = results.get(0).getId();
					lastTweet = results.get(results.size() - 1).getId();
				}
				loading = false;
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Log.d("DEBUG", arg0.getMessage());
			}
		});
	}
	
	@Override
	public void onRefreshHandler() {
		if(!loading) {
        	loading = true;
        	MyTwitterApp.getRestClient().getMentionsTimeline(firstTweet, true, new JsonHttpResponseHandler() {
    			@Override
    			public void onSuccess(JSONArray jsonTweets) {
    				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
    				results.addAll(0, tweets);
    				adapter.notifyDataSetChanged();
    				if(tweets.size() > 0) {
    					firstTweet = tweets.get(0).getId();
    				}
    				loading = false;
    				tweetView.onRefreshComplete();
    			}
    			
    			@Override
    			public void onFailure(Throwable arg0) {
    				Log.d("DEBUG", arg0.getMessage());
    				loading = false;
    				tweetView.onRefreshComplete();
    			}
    		});
        }
		else {
			tweetView.onRefreshComplete();
		}
	}
	
	@Override
	public void onScrollHandler(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
        if ((lastInScreen == totalItemCount) && !(loading) && !disableAutoRefresh) {
        	loading = true;
        	MyTwitterApp.getRestClient().getMentionsTimeline(lastTweet, false, new JsonHttpResponseHandler() {
    			@Override
    			public void onSuccess(JSONArray jsonTweets) {
    				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
    				results.addAll(tweets);
    				adapter.notifyDataSetChanged();
    				if(tweets.size() > 0) {
    					lastTweet = tweets.get(tweets.size() - 1).getId();
    				}
    				else{
    					disableAutoRefresh = true;
    				}
    				loading = false;
    			}
    			
    			@Override
    			public void onFailure(Throwable arg0) {
    				Log.d("DEBUG", arg0.getMessage());
    				loading = false;
    			}
    		});
        }
	}
}
