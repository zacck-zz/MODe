package com.semasoft.MODe;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MODeActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	ImageButton btSearch, btShare, btShuffle, btArtists, btSongs, btAlbums,
			btGenres;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_pad);
		init();
		AdView mAd = (AdView) findViewById(R.id.adView);
		// LinearLayout mlay = (LinearLayout)findViewById(R.id.adLayout);
		// mlay.addView(mAd);
		mAd.loadAd(new AdRequest());
		// AdRequest.addTestDevice("F60EA49895175ADF3D87544280436254");

	}

	private void init() {
		// TODO Auto-generated method stub

		btSearch = (ImageButton) findViewById(R.id.btsearch);
		btSearch.setOnClickListener(this);
		btShare = (ImageButton) findViewById(R.id.btshare);
		btShare.setOnClickListener(this);
		btShuffle = (ImageButton) findViewById(R.id.btshuffle);
		btShuffle.setOnClickListener(this);
		btArtists = (ImageButton) findViewById(R.id.btArtists);
		btArtists.setOnClickListener(this);
		btSongs = (ImageButton) findViewById(R.id.btSongs);
		btSongs.setOnClickListener(this);
		btAlbums = (ImageButton) findViewById(R.id.btAlbums);
		btAlbums.setOnClickListener(this);
		btGenres = (ImageButton) findViewById(R.id.btgenres);
		btGenres.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String param = null;
		Intent small_list = new Intent(MODeActivity.this, ListAct.class);
		switch (v.getId()) {
		case R.id.btArtists:
			Log.v("button", "artist button pressed");
			param = "artSongs";
			small_list.putExtra("par", param);
			startActivity(small_list);
			break;
		case R.id.btSongs:
			param = "songs";
			small_list.putExtra("par", param);
			startActivity(small_list);
			break;
		case R.id.btAlbums:
			param = "albums";
			small_list.putExtra("par", param);
			startActivity(small_list);
			break;
		case R.id.btgenres:
			param = "genre";
			Log.v("press", "genres pressed");
			small_list.putExtra("par", param);
			startActivity(small_list);
			break;
		case R.id.btshare:
			startActivity(new Intent(MODeActivity.this, Shar.class));
			break;
		case R.id.btsearch:
			startActivity(new Intent(MODeActivity.this, SearchPage.class));
			break;

		}
		

	}
}