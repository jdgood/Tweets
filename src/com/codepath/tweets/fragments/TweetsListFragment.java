package com.codepath.tweets.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.codepath.tweets.R;
import com.codepath.tweets.adapters.TweetAdapter;
import com.codepath.tweets.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	protected TweetAdapter adapter;
	protected ArrayList<Tweet> results;
	
	protected PullToRefreshListView tweetView;
	protected boolean loading = true;
	protected boolean disableAutoRefresh = false;
	protected long firstTweet = 0;
	protected long lastTweet = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		tweetView = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		tweetView.setOnScrollListener(new OnScrollListener() {
		    @Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {

		    }

		    @Override
		    public void onScroll(AbsListView view, int firstVisibleItem,
		            int visibleItemCount, int totalItemCount) {
		        onScrollHandler(view, firstVisibleItem, visibleItemCount, totalItemCount);
		    }
		});
		tweetView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            	onRefreshHandler();
            }
        });
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.fragment_tweets_list, container, false);
		return v;
	}
	
	public void onRefreshHandler() {
	}
	
	public void onScrollHandler(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
	}
}
