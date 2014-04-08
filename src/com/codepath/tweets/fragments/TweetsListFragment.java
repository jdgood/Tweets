package com.codepath.tweets.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.codepath.tweets.R;
import com.codepath.tweets.adapters.TweetAdapter;
import com.codepath.tweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	protected TweetAdapter adapter;
	protected ArrayList<Tweet> results;
	protected ListView lvTweets;
	
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
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		return v;
	}
	
	public abstract void onRefreshHandler();
	
	public abstract void onScrollHandler(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount);
	
	protected class InitialHandler extends JsonHttpResponseHandler {
		@Override
		public void onSuccess(JSONArray jsonTweets) {
			results = Tweet.fromJson(jsonTweets);
			
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
	}
	
	protected class RefreshHandler extends JsonHttpResponseHandler {
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
	}
	
	protected class ScrollHandler extends JsonHttpResponseHandler {
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
	}
}
