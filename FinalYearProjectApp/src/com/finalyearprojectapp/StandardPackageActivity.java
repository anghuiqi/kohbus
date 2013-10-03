package com.finalyearprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class StandardPackageActivity extends Activity implements OnClickListener{
	RadioButton packageGroup;
	
	public void onCreate(Bundle savedInstancesState){
		super.onCreate(savedInstancesState);
		setContentView(R.layout.standard_package);
        
		Button mBtn1 = (Button) findViewById(R.id.NextButton);
        mBtn1.setOnClickListener(this);
 
	}
	
	@Override
	public void onClick(View v) {	
		Intent i=new Intent(StandardPackageActivity.this, DateTimeSelectionActivity.class);
		startActivity(i);
	}
}
