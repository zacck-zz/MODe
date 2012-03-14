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

import com.semasoft.MODe.ArtistSongs.getSongsArtists;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SongsList extends ListActivity {
	String a_id;
	String[] song_links = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		a_id = getIntent().getExtras().getString("art");
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

			SimpleAdapter adp = new SimpleAdapter(SongsList.this, listy,
					R.layout.list_item_detail, new String[] { "songName",
							"songID", "artName" }, new int[] {
							R.id.tvSongName, R.id.tvAlbum, R.id.tvArtist});
			setListAdapter(adp);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					
					Intent n =  new Intent(SongsList.this, Player.class);
					
					if (song_links!=null) {
						n.putExtra("url", song_links[pos]);
						n.putExtra("aname", artist_name[pos]);
						n.putExtra("sname", song_name[pos]);
						startActivity(n);
					}
					 
					
					

					
				}
			});
		}

	}


}
