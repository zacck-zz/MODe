package com.semasoft.MODe;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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



import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SearchList extends ListActivity {
	String param;
	ProgressBar pb;
	int Prog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		pb = (ProgressBar)findViewById(R.id.pbSearch);
		param = getIntent().getExtras().getString("param");
		
		getSongs g = new getSongs();
		g.execute();
		
		
	}
	
	class getSongs extends AsyncTask<Void, Integer, Void> {
		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();
		String[] song_genre, artist_name, song_name, song_links;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://semasoftltd.com/dwebservices/search.php");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("param", param));
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse resp = client.execute(request);
				
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");
				Log.v("json", "PARENT" + parent.toString());

				song_genre = new String[parent.length()];
				song_name = new String[parent.length()];
				artist_name = new String[parent.length()];
				song_links = new String[parent.length()];
				for (int i = 0; i < parent.length(); i++) {
					JSONObject child = parent.getJSONObject(i).getJSONObject(
							"post");
					song_genre[i] = child.getString("song_genre");
					song_name[i] = child.getString("song_name");
					artist_name[i] = child.getString("artist_name");
					song_links[i] = child.getString("songs_link");
					Prog = i;
					publishProgress(i);
				}
				HashMap<String, String> hash;
				for (int i = 0; i < parent.length(); i++) {
					hash = new HashMap<String, String>();
					hash.put("songName", song_name[i]);
					hash.put("songID", song_genre[i]);
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

			SimpleAdapter adp = new SimpleAdapter(SearchList.this, listy,
					R.layout.list_item_detail, new String[] { "songName",
							"songID", "artName" }, new int[] {
							R.id.tvSongName, R.id.tvAlbum, R.id.tvArtist });
			setListAdapter(adp);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					
					
					Intent in = new Intent(SearchList.this, Player.class);
					in.putExtra("url", song_links[pos]);
					startActivity(in);
				}
			});
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			pb.setProgress(Prog);
			
		}

	}

}
