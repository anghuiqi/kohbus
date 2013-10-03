package com.finalyearprojectapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LocationSelectionActivity extends Activity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_selection);
        
		Button mBtn1 = (Button) findViewById(R.id.NextButton);
        mBtn1.setOnClickListener(this);
    	//final ToggleButton togglebutton = (ToggleButton) findViewById(R.layout.location_selection);
    	//togglebutton.setOnClickListener(this);
	}
	    
	@Override
	public void onClick(View v) {	
		Intent i=new Intent(LocationSelectionActivity.this, DateTimeSelectionActivity.class);
		startActivity(i);
	}

}
