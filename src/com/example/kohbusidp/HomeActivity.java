package com.example.kohbusidp;

import com.example.kohbusidp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity implements OnClickListener{

	private Button timeBookingButton;
	private Button tripBookingButton;
	private Button makeBookingButton;
	public final static String EXTRA_MESSAGE = "com.example.kohbusidp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		getActionBar().hide();
		//instantiate the buttons
		timeBookingButton = (Button) findViewById(R.id.timeBasedBooking);
		tripBookingButton = (Button) findViewById(R.id.tripBasedBooking);		
		makeBookingButton = (Button) findViewById(R.id.makeABooking);


		ImageView home_image = (ImageView) findViewById(R.id.homepage);
		//************************the intents
		//for BOTH METHOD should link to a new file EACH that has an implementation of the tabhostactivity.
		
		makeBookingButton.setOnClickListener(new OnClickListener(){	
			public void onClick(View v){
				if(timeBookingButton.getVisibility() == View.VISIBLE){
					timeBookingButton.setVisibility(View.GONE);
					tripBookingButton.setVisibility(View.GONE);
				}else{
					timeBookingButton.setVisibility(View.VISIBLE);
					tripBookingButton.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		timeBookingButton.setOnClickListener(new OnClickListener(){	
			//Calls the login and authenticates the email & password
			public void onClick(View v){
				Intent intent = new Intent(HomeActivity.this, MakeABookingActivity.class);
				String message = "timebased";
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
		    }
		});
		
		tripBookingButton.setOnClickListener(new OnClickListener(){	
			//Calls the login and authenticates the email & password
			public void onClick(View v){
				Intent intent = new Intent(HomeActivity.this, MakeABookingActivity.class);
				String message = "tripbased";
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
				
		    }
		});
		
	}
	
	@Override //when inheriting onclicklistener, there must be an onClick(View v) method.
	public void onClick(View v) {
	    // TODO Auto-generated method stub

	}

}
