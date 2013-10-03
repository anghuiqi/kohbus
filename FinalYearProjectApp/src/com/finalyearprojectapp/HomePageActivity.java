package com.finalyearprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
 
public class HomePageActivity extends Activity{
	Button viewBusesButton,makeABookingButton, bkgHistoryButton;
    Button standardTripButton, customTripButton;
    LinearLayout panel1,panel2;
     
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
       
        viewBusesButton = (Button)findViewById(R.id.viewBuses);
        makeABookingButton = (Button)findViewById(R.id.makeABooking);
        bkgHistoryButton = (Button)findViewById(R.id.bkgHistory);
        
        standardTripButton = (Button)findViewById(R.id.standardTrip);
        customTripButton = (Button)findViewById(R.id.customTrip);
       
        viewBusesButton.setOnClickListener(buttonClickListener);
        makeABookingButton.setOnClickListener(buttonClickListener);
        bkgHistoryButton.setOnClickListener(buttonClickListener);
       
        standardTripButton.setOnClickListener(buttonClickListener);
        customTripButton.setOnClickListener(buttonClickListener);
        
        panel1 = (LinearLayout)findViewById(R.id.panel1);
        panel1.setVisibility(View.GONE);
    }
   
   
    public OnClickListener buttonClickListener = new OnClickListener(){
    	@Override
        public void onClick(View v){
    		Button ClickedButton = (Button)v;
    		if (ClickedButton.getId() == makeABookingButton.getId()){
    			panel1.setVisibility(View.VISIBLE);
            } else if(ClickedButton.getId() == standardTripButton.getId()){
            	Intent i = new Intent(HomePageActivity.this, StandardPackageActivity.class);
                startActivity(i);
            }else if(ClickedButton.getId() == customTripButton.getId()){
            	Intent i = new Intent(HomePageActivity.this, LocationSelectionActivity.class);
                startActivity(i);
            }
    	}
    };
}
