package com.codepath.tweets;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.codepath.tweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	public static TimelineActivity instance;
	
	private TweetAdapter adapter;
	private ArrayList<Tweet> results;
	
	PullToRefreshListView tweetView;
	private boolean loading = true;
	private long firstTweet = 0;
	private long lastTweet = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		instance = this;
		
		setupActionbar();
		
		tweetView = (PullToRefreshListView) findViewById(R.id.lvTweets);
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
		
		MyTwitterApp.getRestClient().getHomeTimeline(0, true, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				results = Tweet.fromJson(jsonTweets);
				
				ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
				adapter = new TweetAdapter(getBaseContext(), results);
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
	
	public void onRefreshHandler() {
		if(!loading) {
        	loading = true;
        	MyTwitterApp.getRestClient().getHomeTimeline(firstTweet, true, new JsonHttpResponseHandler() {
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
	
	public void onScrollHandler(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
        if ((lastInScreen == totalItemCount) && !(loading)) {
        	loading = true;
        	MyTwitterApp.getRestClient().getHomeTimeline(lastTweet, false, new JsonHttpResponseHandler() {
    			@Override
    			public void onSuccess(JSONArray jsonTweets) {
    				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
    				results.addAll(tweets);
    				adapter.notifyDataSetChanged();
    				if(tweets.size() > 0) {
    					lastTweet = tweets.get(tweets.size() - 1).getId();
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
	
	private void setupActionbar() {
		ActionBar actionBar = getActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.search_bar);
	    
	    Button btNewTweet = (Button) actionBar.getCustomView().findViewById(R.id.btNewTweet);
	    btNewTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onNewTweet(v);
			}
		});
	    
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);// | ActionBar.DISPLAY_SHOW_HOME);
	}
	
	public void onNewTweet(View v) {
		DialogFragment newFragment = new TweetDialog();
	    newFragment.show(getFragmentManager(), "new_tweet");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
