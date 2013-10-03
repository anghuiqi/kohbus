package com.finalyearprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ContactInfoActivity extends Activity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_selection);
        
        Button mBtn1 = (Button) findViewById(R.id.submitButton);
        mBtn1.setOnClickListener(this);      
    }
    
    public void onClick(View view){
		 Intent i=new Intent(this, HomePageActivity.class);
	     startActivity(i);
    }

}