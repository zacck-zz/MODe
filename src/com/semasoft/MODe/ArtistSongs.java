package com.semasoft.MODe;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ArtistSongs extends ListActivity {

	String Artist, id;
	Intent in;
	String[] song_links = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		Artist = getIntent().getExtras().getString("n");
		id = getIntent().getExtras().getString("id");
		getSongsArtists g = new getSongsArtists();
		g.execute();

	}

	class getSongsArtists extends AsyncTask<Void, Void, Void> {
		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();
		String[] song_id, artist_name, song_name;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(
						"http://semasoftltd.com/dwebservices/songs_query.php?v=artSongs"));
				HttpResponse resp = client.execute(request);
				// handle the response
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");
				Log.v("json", "PARENT" + parent.toString());

				song_id = new String[parent.length()];
				song_name = new String[parent.length()];
				artist_name = new String[parent.length()];
				song_links = new String[parent.length()];
				for (int i = 0; i < parent.length(); i++) {
					JSONObject child = parent.getJSONObject(i).getJSONObject(
							"post");
					song_id[i] = child.getString("song_id");
					song_name[i] = child.getString("song_name");
					artist_name[i] = child.getString("artist_name");
					song_links[i] = child.getString("songs_link");
				}
				HashMap<String, String> hash;
				for (int i = 0; i < parent.length(); i++) {
					hash = new HashMap<String, String>();
					hash.put("songName", song_name[i]);
					hash.put("songID", song_id[i]);
					hash.put("artName", artist_name[i]);
					hash.put("link", song_links[i]);
					listy.add(hash);

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);

			SimpleAdapter adp = new SimpleAdapter(ArtistSongs.this, listy,
					R.layout.list_item_detail, new String[] { "songName",
							"songID", "artName" }, new int[] {
							R.id.tvSongName, R.id.tvAlbum, R.id.tvArtist});
			setListAdapter(adp);

//			ListView lv = getListView();
//			lv.setTextFilterEnabled(true);
//
//			lv.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int pos, long arg3) {
//					
//					
//					 
//					
//					
//
//					//starterservi s = new starterservi();
//					//s.execute();
//				}
//			});
		}

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		Intent n =  new Intent(ArtistSongs.this, Player.class);
		
		if (song_links!=null) {
			n.putExtra("url", song_links[position]);
			startActivity(n);
		}
		
		
	}

	
	
	

}
