package com.semasoft.MODe;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Player extends Activity {
	
	String url;
	Intent in;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_view);
		
		AdView mAd = (AdView)findViewById(R.id.ad);
		mAd.loadAd(new AdRequest());
		
		url = getIntent().getExtras().getString("url");
		Log.v("server", url);
		
		//check if the service is running 
		if(!isMyServiceRunning()){
			AlertDialog.Builder mBuilder = new Builder(Player.this);
			mBuilder.setCancelable(false);
			mBuilder.setTitle("Action");
			mBuilder.setMessage("Do you want to stop the current song or add this one to the playlist");
			mBuilder.setPositiveButton("Add", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			mBuilder.setNegativeButton("Stop", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			
			AlertDialog mPlayDialog = mBuilder.create();
			
		}
		else
		{
		starterservi s = new starterservi();
		s.execute();
		}
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.play_menu, menu);
		return true;
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.stopPlaying:
			Intent n = new Intent(Player.this, Mplayer.class);
			Toast.makeText(Player.this, "pressed me ", Toast.LENGTH_LONG).show();
			stopService(n);
			break;
		case R.id.invite:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mode Music Player");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Entertainment on Demand");

			startActivity(Intent.createChooser(shareIntent, "Listening to Music on MODe"));

		break;

		
		}
		return super.onOptionsItemSelected(item);
		
	}
	class starterservi extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			in = new Intent(Player.this, Mplayer.class);
			in.putExtra("url", url);			
			if (!isMyServiceRunning()) {
				startService(in);
			} else {
				
				
			}

			
			return null;
		}
		
	}
	
	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.semasoft.MODe.Mplayer".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
