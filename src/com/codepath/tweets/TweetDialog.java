package com.codepath.tweets;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetDialog extends DialogFragment {
	private EditText etBody;
	private TextView tvRemaining;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.tweet_dialog, null);
		setupViews(v);
		
		builder.setView(v);
		builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//submit tweet, refresh view
					MyTwitterApp.getRestClient().sendTweet(etBody.getText().toString(), new JsonHttpResponseHandler() {
		    			@Override
		    			public void onSuccess(JSONObject jsonTweet) {
							TimelineActivity.instance.onRefreshHandler();
		    			}
		    			
		    			@Override
		    			public void onFailure(Throwable arg0) {
		    				Log.d("DEBUG", arg0.getMessage());
		    			}
		    		});
				}
			});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					TweetDialog.this.getDialog().cancel();
				}
			});
		
		return builder.create();
	}
	
	private void setupViews(View v) {
		tvRemaining = (TextView) v.findViewById(R.id.tvRemaining);
		tvRemaining.setText("140 Characters Remaining");
		setupBody(v);
	}
	
	private void setupBody(View v) {
		etBody = (EditText) v.findViewById(R.id.etBody);
		etBody.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() != KeyEvent.ACTION_DOWN) {
					return true;
				}
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					return true;
				}
				else if(keyCode != KeyEvent.KEYCODE_DEL && etBody.getText().length() == 140) {
					return true;
				}
				
				int length = etBody.getText().length();
				
				if(keyCode == KeyEvent.KEYCODE_DEL) {
					length--;
				}
				else {
					length++;
				}
				tvRemaining.setText(140 - length + " Characters Remaining");
				return false;
			}
		});
	}
}
