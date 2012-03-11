package com.semasoft.MODe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SearchPage extends Activity implements OnClickListener {
	EditText etSearch;
	Button btSearchPageButton;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_page);
		etSearch = (EditText)findViewById(R.id.etSearchParam);
		btSearchPageButton = (Button)findViewById(R.id.btSearchdb);
		btSearchPageButton.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btSearchdb:
			Intent n = new  Intent(SearchPage.this, SearchList.class);
			n.putExtra("param", etSearch.getText().toString());
			startActivity(n);
			
			break;
		}
	}
	
	

}
