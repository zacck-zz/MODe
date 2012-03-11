package com.semasoft.MODe;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;

import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Mplayer extends Service {
	static boolean isPlaying = false;
	public MediaPlayer mp;
	String url = null;

	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		isPlaying = false;
	}

	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "Music stopping", 1).show();
		
		mp.stop();
		mp.release();
		isPlaying = false;
	}

	public void onStart(Intent paramIntent, int paramInt) {
		super.onStart(paramIntent, paramInt);
		Toast.makeText(getApplicationContext(), "Getting the Best", 1).show();
		url = ((String) paramIntent.getExtras().getString("url"));	
		
		
		
		play p = new play();
		p.execute();
		
		
	}
	

	public class play extends AsyncTask<Void, Integer, Void> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			Log.v("service", "starting jobo");
			mp = new MediaPlayer();
			mp.setAudioStreamType(3);
			
			
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Log.v("service", url);
				mp.setDataSource(url);
				Log.v("service", "datasourse set");
				mp.prepareAsync();
				
				Log.v("service", "prepared");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("serviceError", e.toString());
			
			}
			
			mp.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer player) {
					
					Log.v("service","going in ");
					player.getDuration();
					player.start();
					Log.v("duration", player.getDuration()+" ");
					for(int i=0; i<player.getDuration(); player.getCurrentPosition())
					{
						//Log.v("position", "pos"+player.getCurrentPosition());
					}

					
					Log.v("service", "playing");
					
				}
			});
			
			return null;
		}
		
		
		
		
		
		

	}
	
	public void  mpResetLoadUrl(String u)
	{
		mp.reset();
		url = u;
		play pl = new play();
		pl.execute();
		
		
	}
	
		
	
}
