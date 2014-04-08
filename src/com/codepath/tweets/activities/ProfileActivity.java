package com.codepath.tweets.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tweets.MyTwitterApp;
import com.codepath.tweets.R;
import com.codepath.tweets.fragments.UserTimelineFragment;
import com.codepath.tweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends FragmentActivity {
	private UserTimelineFragment userTimeline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Intent i = getIntent();
		String user = i.getStringExtra(TimelineActivity.USER_EXTRA);
		loadProfileInfo(user);
		userTimeline = new UserTimelineFragment(user);
		getSupportFragmentManager().beginTransaction().replace(R.id.flUserTimelineContainer, userTimeline).commit();
	}

	private void loadProfileInfo(String user) {
		MyTwitterApp.getRestClient().getUserInfo(user, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User u = User.fromJson(json);
				getActionBar().setTitle("@" + u.getScreenName());
				
				populateProfileHeader(u);
			}

			@Override
			public void onFailure(Throwable arg0) {
				Log.d("DEBUG", arg0.getMessage());
			}
		});
	}
	
	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		
		Picasso.with(this).load(u.getProfileImageUrl()).into(ivProfileImage);
	}
	
	public void onProfile(String user) {
		Intent i = new Intent(this, ProfileActivity.class);
		if(user != null) {
			i.putExtra(TimelineActivity.USER_EXTRA, user);
		}
		startActivity(i);
	}
	
	public void loadUserTimeline(View v) {
		onProfile((String) v.getTag());
	}
}
