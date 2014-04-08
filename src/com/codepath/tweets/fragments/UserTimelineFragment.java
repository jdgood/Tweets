package com.codepath.tweets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.codepath.tweets.MyTwitterApp;

public class UserTimelineFragment extends TweetsListFragment {
	private String user;
	
	public UserTimelineFragment() {
	}
	
	public UserTimelineFragment(String user) {
		this.user = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		MyTwitterApp.getRestClient().getUserTimeline(0, true, user, new InitialHandler());
		
		return v;
	}
	
	@Override
	public void onRefreshHandler() {
		if(!loading) {
        	loading = true;
        	MyTwitterApp.getRestClient().getUserTimeline(firstTweet, true, user, new RefreshHandler());
        }
		else {
			tweetView.onRefreshComplete();
		}
	}
	
	@Override
	public void onScrollHandler(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
        if ((lastInScreen == totalItemCount) && !loading && !disableAutoRefresh) {
        	loading = true;
        	MyTwitterApp.getRestClient().getUserTimeline(lastTweet, false, user, new ScrollHandler());
        }
	}
}
