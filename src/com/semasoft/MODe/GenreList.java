package com.semasoft.MODe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class GenreList extends ListActivity {

	String gen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		gen = getIntent().getExtras().getString("gen");
		Toast.makeText(GenreList.this, gen, Toast.LENGTH_LONG).show();
		fetchgenreArtsist f = new fetchgenreArtsist();
		f.execute();
		

	}

	class fetchgenreArtsist extends AsyncTask<Void, Void, Void> {

		ArrayList<HashMap<String, String>> listy = new ArrayList<HashMap<String, String>>();
		String[]  artist_names,artist_id;
		String urlFetch = "http://semasoftltd.com/dwebservices/songs_list_genres.php";

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"http://semasoftltd.com/dwebservices/songs_list_genres.php");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("param", gen));
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse resp = client.execute(request);
				JSONArray parent = new JSONObject(EntityUtils.toString(resp
						.getEntity())).getJSONArray("songs");

				artist_names = new String[parent.length()];
				artist_id = new String[parent.length()];
				

				JSONObject child;
				for (int i = 0; i < parent.length(); i++) {
					child = parent.getJSONObject(i).getJSONObject("post");

					artist_names[i] = child.getString("artist_name");
					Log.v("jsonart", artist_names[i]);
					artist_id[i] = child.getString("artist_id");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			setListAdapter(new ArrayAdapter<String>(GenreList.this, android.R.layout.simple_list_item_1,artist_names));
			
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {

				}
			});
		}

	}

}
