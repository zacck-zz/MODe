package com.semasoft.MODe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class Rating_Bar extends Activity implements RatingBar.OnRatingBarChangeListener{

	RatingBar rating; // declare RatingBar object
	TextView ratingText;// declare TextView Object

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call super class constructor

		setContentView(R.layout.rating);// set content from main.xml
		ratingText=(TextView)findViewById(R.id.ratingText);// create TextView object
		rating=(RatingBar)findViewById(R.id.ratingBar);// create RatingBar object
		rating.setOnRatingBarChangeListener(this);// select listener to be HelloAndroid (this) class
	}

	

	@Override
	public void onRatingChanged(RatingBar ratingB, float rating, boolean fromUser) {
		// TODO Auto-generated method stub
		ratingText.setText(""+this.rating.getRating());
		
	}
}