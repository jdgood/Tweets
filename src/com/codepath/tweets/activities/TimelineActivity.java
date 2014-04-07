package com.codepath.tweets.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.codepath.tweets.ProfileActivity;
import com.codepath.tweets.R;
import com.codepath.tweets.fragments.HomeTimelineFragment;
import com.codepath.tweets.fragments.MentionsFragment;
import com.codepath.tweets.fragments.TweetDialog;

public class TimelineActivity extends FragmentActivity implements TabListener {
	public static HomeTimelineFragment homeTimeline;
	public static MentionsFragment mentionsTimeline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		homeTimeline = new HomeTimelineFragment();
		mentionsTimeline = new MentionsFragment();
		
		setupActionbar();
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
	    
	    Button btProfile = (Button) actionBar.getCustomView().findViewById(R.id.btProfile);
	    btProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onProfile(null);
			}
		});
	    
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    Tab tabHome = actionBar.newTab().setText("Home")
	    		.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_launcher)
	    		.setTabListener(this);
	    
	    Tab tabMentions = actionBar.newTab().setText("Mentions")
	    		.setTag("MentionsFragment").setIcon(R.drawable.ic_launcher)
	    		.setTabListener(this);
	    
	    actionBar.addTab(tabHome);
	    actionBar.addTab(tabMentions);
	    actionBar.selectTab(tabHome);
	}
	
	public void onNewTweet(View v) {
		DialogFragment newFragment = new TweetDialog();
	    newFragment.show(getFragmentManager(), "new_tweet");
	}
	
	public void onProfile(String user) {
		if(user == null) {
			Intent i = new Intent(this, ProfileActivity.class);
			startActivity(i);
		}
		else {
			
		}
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		android.support.v4.app.FragmentTransaction fts = getSupportFragmentManager()
				.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.flContainer, homeTimeline);
		}
		else {
			fts.replace(R.id.flContainer, mentionsTimeline);
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
}
