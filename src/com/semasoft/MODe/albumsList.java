package com.semasoft.MODe;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class albumsList extends ListActivity  {
	
	String art, alb;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		art = getIntent().getExtras().getString("art");
		alb = getIntent().getExtras().getString("album");
		
		fetchAlbumsongs f = new fetchAlbumsongs();
		f.execute();
		
		
		
	}
	
	class fetchAlbumsongs extends AsyncTask<Void, Void, Void> {

		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();
		String[] album_names, artist_names, song_links, song_names;
		 String urlFetch = "http://semasoftltd.com/dwebservices/songs_albums_list.php";
		 

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://semasoftltd.com/dwebservices/songs_list_albums.php");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("paramart", art));
		        nameValuePairs.add(new BasicNameValuePair("paramalb", alb));
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse resp = client.execute(request);
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");

				album_names = new String[parent.length()];
				artist_names = new String[parent.length()];
				song_links = new String[parent.length()];
				song_names = new String[parent.length()];
				JSONObject child;
				for (int i = 0; i < parent.length(); i++) {
					child = parent.getJSONObject(i).getJSONObject("post");

					album_names[i] = child.getString("song_album");
					artist_names[i] = child.getString("artist_name");
					song_links[i] = child.getString("songs_link");
					song_names[i] = child.getString("song_name");
				}

				HashMap<String, String> hash;
				for (int i = 0; i < parent.length(); i++) {
					hash = new HashMap<String, String>();
					hash.put("albumNames", album_names[i]);
					hash.put("artistNames", artist_names[i]);
					hash.put("songNames", song_names[i]);
					hash.put("songLink", song_links[i]);
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

			SimpleAdapter adp = new SimpleAdapter(albumsList.this, listy,
					R.layout.list_item_detail,
					new String[] { "albumNames","artistNames","songNames" }, 
					new int[] { R.id.tvArtist,R.id.tvAlbum, R.id.tvSongName });
			setListAdapter(adp);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					String me = song_links[pos];
					Intent n = new Intent(albumsList.this, Player.class);
					n.putExtra("url", me);
								
				}
			});
		}

	}

	

}
