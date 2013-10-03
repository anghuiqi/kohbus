package com.finalyearprojectapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class DateTimeSelectionActivity extends Activity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_selection);
        
        Button mBtn1 = (Button) findViewById(R.id.nextbutton);
        mBtn1.setOnClickListener(this);      
    }
    
    public void onClick(View view){
		 Intent i=new Intent(this, ContactInfoActivity.class);
	     startActivity(i);
    }

}
