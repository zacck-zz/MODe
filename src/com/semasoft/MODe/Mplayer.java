package com.semasoft.MODe;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.PowerManager;

import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Mplayer extends Service implements OnAudioFocusChangeListener {
	static boolean isPlaying = false;
	public MediaPlayer mp;
	String url = null;
	WifiLock wLock;
	play p;
	static final int NOTIFICATION_ID = 1;

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
		wLock.release();
		stopForeground(true);
		isPlaying = false;
	}

	public void onStart(Intent paramIntent, int paramInt) {
		super.onStart(paramIntent, paramInt);
		Toast.makeText(getApplicationContext(), "Getting the Best", 1).show();
		
		url = ((String) paramIntent.getExtras().getString("url"));
		
		p = new play();
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
			mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
			wLock = ((WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL,"MODeWifiLock");
			wLock.acquire();
			
			
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
					if(audioChecker())
					{
					player.start();
					PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
			                new Intent(getApplicationContext(), Player.class),
			                PendingIntent.FLAG_UPDATE_CURRENT);
					Notification notification = new Notification();
					notification.tickerText = url +" is playing";
					notification.icon = R.drawable.dashboard_icon;
					notification.flags |= Notification.FLAG_ONGOING_EVENT;
					notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
					                "Playing: " + url, pi);
					startForeground(NOTIFICATION_ID, notification);
					}
					else
					{
						Log.v("service", "playing isnt possible audio is playing");
					}
					Log.v("duration", player.getDuration()+" ");
					
					
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
	
	public boolean audioChecker()
	{
		AudioManager am = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
		int res = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		if (res != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
		{
			return false;
		}
		else{
			return true;	
		}
		
		
	}

	@Override
	public void onAudioFocusChange(int focus) {
		switch(focus)
		{
		case AudioManager.AUDIOFOCUS_GAIN:
            // resume playback
            if (mp == null) p.execute();
            else if (!mp.isPlaying()) mp.start();
            mp.setVolume(1.0f, 1.0f);
            break;

        case AudioManager.AUDIOFOCUS_LOSS:
            // Lost focus for an unbounded amount of time: stop playback and release media player
            if (mp.isPlaying()) mp.stop();
            
            mp.release();
            
            mp = null;
            stopForeground(true);
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            // Lost focus for a short time, but we have to stop
            // playback. We don't release the media player because playback
            // is likely to resume
            if (mp.isPlaying())mp.pause();
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            // Lost focus for a short time, but it's ok to keep playing
            // at an attenuated level
            if (mp.isPlaying()) mp.setVolume(0.1f, 0.1f);
            break;
		}
		
		
		
	}

	
	
		
	
}
