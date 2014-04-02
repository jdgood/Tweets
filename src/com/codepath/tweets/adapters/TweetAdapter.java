package com.codepath.tweets.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tweets.R;
import com.codepath.tweets.R.id;
import com.codepath.tweets.R.layout;
import com.codepath.tweets.models.Tweet;
import com.squareup.picasso.Picasso;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	public TweetAdapter(Context context, List<Tweet> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b><small><font color='#777777'> @" +
				tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		TextView timeView = (TextView) view.findViewById(R.id.tvTime);
		
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss Z yyyy", Locale.ENGLISH);
	    Date time;
		try {
			time = df.parse(tweet.getTimestamp());
		} catch (Exception e) {
			e.printStackTrace();
			time = new Date();
		} 
		
		String str = "<small><font color='#777777'>" + DateUtils.getRelativeDateTimeString(getContext(), time.getTime(), 
				DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0) + "</font></small>";
		timeView.setText(Html.fromHtml(str));
		
		return view;
	}
}
