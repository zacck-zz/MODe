package com.semasoft.MODe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListAct extends ListActivity {

	String param;
	String urlFetch;
	TextView tvlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		tvlist = (TextView) findViewById(R.id.tvLoading);
		param = getIntent().getExtras().getString("par");
		urlFetch = "http://semasoftltd.com/dwebservices/songs_query.php?v="
				+ param;
		if (param.equals("songs")) {
			fetchSongs f = new fetchSongs();
			f.execute();
		} else if (param.equals("artSongs")) {
			urlFetch = "http://semasoftltd.com/dwebservices/artist_query.php";
			fetchArtistSongs f = new fetchArtistSongs();
			f.execute();
		} else if (param.equals("albums")) {
			fetchAlbums f = new fetchAlbums();
			f.execute();
		} else if (param.equals("genre")) {
			fetchGenres f = new fetchGenres();
			f.execute();
		}
	}

	class fetchArtistSongs extends AsyncTask<Void, Void, Void> {

		String[] artists = null;
		String[] art_ids = null;

		@Override
		protected Void doInBackground(Void... params) {

			tvlist.setText("Loading Songs");

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(urlFetch));
				HttpResponse resp = client.execute(request);
				// handle the response
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("artists");
				Log.v("json", "PARENT" + parent.toString());

				artists = new String[parent.length()];
				art_ids = new String[parent.length()];
				for (int i = 0; i < parent.length(); i++) {
					JSONObject child = parent.getJSONObject(i).getJSONObject(
							"post");
					Log.v("json", child.toString());
					artists[i] = child.getString("artist_name");
					Log.v("json", child.getString("artist_name"));
					art_ids[i] = child.getString("artist_id");

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
			tvlist.setText("songs found");
			setListAdapter(new ArrayAdapter<String>(ListAct.this,
					android.R.layout.simple_list_item_1, artists));
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					String a_name = artists[pos];
					String a_id = art_ids[pos];
					Intent n = new Intent(ListAct.this, ArtistSongs.class);
					n.putExtra("n", a_name);
					n.putExtra("id", a_id);
					startActivity(n);

				}
			});

		}

	}

	class fetchGenres extends AsyncTask<Void, Void, Void> {
		String[] mGenres;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(urlFetch));
				HttpResponse resp = client.execute(request);
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");
				mGenres = new String[parent.length()];
				JSONObject child;
				for (int i = 0; i < parent.length(); i++) {
					child = parent.getJSONObject(i).getJSONObject("post");
					mGenres[i] = child.getString("song_genre");
					Log.v("gens", mGenres[i]);
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
			setListAdapter(new ArrayAdapter<String>(ListAct.this,
					android.R.layout.simple_list_item_1, mGenres));
			ListView lv = getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					Intent n = new Intent(ListAct.this, GenreList.class);
					n.putExtra("gen", mGenres[pos]);
					startActivity(n); 
				}
			});
		}
	}

	class fetchAlbums extends AsyncTask<Void, Void, Void> {

		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();

		String[] album_names, artist_names;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(urlFetch));
				Log.v("backgroud", urlFetch);
				HttpResponse resp = client.execute(request);
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");

				album_names = new String[parent.length()];
				artist_names = new String[parent.length()];
				JSONObject child;
				for (int i = 0; i < parent.length(); i++) {
					child = parent.getJSONObject(i).getJSONObject("post");

					album_names[i] = child.getString("song_album");
					artist_names[i] = child.getString("artist_name");
				}

				HashMap<String, String> hash;
				for (int i = 0; i < parent.length(); i++) {
					hash = new HashMap<String, String>();
					hash.put("albumNames", album_names[i]);
					hash.put("artistNames", artist_names[i]);
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

			SimpleAdapter adp = new SimpleAdapter(ListAct.this, listy,
					R.layout.list_item_primary, new String[] { "albumNames",
							"artistNames" }, new int[] { R.id.tvFocus,
							R.id.tvMeta });
			setListAdapter(adp);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
				Intent n = new Intent(ListAct.this, albumsList.class);
				n.putExtra("art", artist_names[pos]);
				n.putExtra("album", album_names[pos]);
				startActivity(n);					
				}
			});
		}

	}

	class fetchSongs extends AsyncTask<Void, Void, Void> {

		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();

		String[] artist_names, song_names, song_ids, album_names;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(urlFetch));
				HttpResponse resp = client.execute(request);
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");

				artist_names = new String[parent.length()];
				song_names = new String[parent.length()];
				song_ids = new String[parent.length()];
				album_names = new String[parent.length()];
				JSONObject child;

				for (int i = 0; i < parent.length(); i++)

				{
					child = parent.getJSONObject(i).getJSONObject("post");
					artist_names[i] = child.getString("artist_name");
					song_names[i] = child.getString("song_name");
					song_ids[i] = child.getString("song_id");
					album_names[i] = child.getString("song_album");

				}

				// load the hashmaps
				HashMap<String, String> hash;
				for (int i = 0; i < parent.length(); i++) {
					hash = new HashMap<String, String>();
					hash.put("songName", song_names[i]);
					hash.put("songID", song_ids[i]);
					hash.put("artName", artist_names[i]);
					hash.put("albumName", album_names[i]);
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
			SimpleAdapter adp = new SimpleAdapter(ListAct.this, listy,
					R.layout.list_item_detail, new String[] { "songName",
							"songID", "artName", "albumName" }, new int[] {
							R.id.tvSongName, R.id.tvGenre, R.id.tvArtist,
							R.id.tvAlbum });
			setListAdapter(adp);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

		}

	}

}
