package com.codepath.tweets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.codepath.tweets.MyTwitterApp;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		MyTwitterApp.getRestClient().getHomeTimeline(0, true, new InitialHandler());
		
		return v;
	}
	
	@Override
	public void onRefreshHandler() {
		if(!loading) {
        	loading = true;
        	MyTwitterApp.getRestClient().getHomeTimeline(firstTweet, true, new RefreshHandler());
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
        	MyTwitterApp.getRestClient().getHomeTimeline(lastTweet, false, new ScrollHandler());
        }
	}
}
