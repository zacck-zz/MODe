package com.semasoft.MODe;

import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Shar extends Activity implements OnClickListener {
	Facebook mfacebook = new Facebook("372314599459769");
	Button fb,Tw;
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    String access_token;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.social_media);
		fb = (Button)findViewById(R.id.socfb);
		fb.setOnClickListener(this);
		mPrefs = getPreferences(MODE_PRIVATE);
		access_token = mPrefs.getString("access_token", null);
		
		
		
		
		
		
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mfacebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.socfb:
			
			long expires = mPrefs.getLong("access_expires", 0);
			if( access_token !=null){
				mfacebook.setAccessToken(access_token);
			}
			if(expires !=0)
			{
				mfacebook.setAccessExpires(expires);
			}
			if(!mfacebook.isSessionValid()){
			mfacebook.authorize(Shar.this,new String[]{"publish_stream","publish_checkins"}, new DialogListener() {
				
				
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
			}
		
			try {
				String response = mfacebook.request("me");
				Bundle upDets = new Bundle();
				upDets.putString("message", "this is a test update");
				upDets.putString("description", "test test test");
				upDets.putString(Facebook.TOKEN, access_token);
				response = mfacebook.request("me/feed", upDets, "POST");
				Toast.makeText(Shar.this, "posted to timeline", Toast.LENGTH_LONG).show();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			break;
		}
		
	}

	@Override
	protected void onResume() {
		 super.onResume();
	     mfacebook.extendAccessTokenIfNeeded(Shar.this, null);
	}

}
