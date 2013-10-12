package com.example.kohbusidp;

import java.util.ArrayList;

import android.app.*;
import android.app.ActionBar.OnNavigationListener;
import android.content.*;
import android.os.Bundle;
import android.text.Layout;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MakeABookingActivity extends TabActivity{
/*	private SessionManagement session;*/
	private TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		tabHost = getTabHost();
		String message = getIntent().getStringExtra(HomeActivity.EXTRA_MESSAGE);
		
		setupActionBar(message);
		setupTab();
		//activate buttons under different tabs
		chooseAction(tabHost.getCurrentTab());
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				chooseAction(tabHost.getCurrentTab());
			}
		});
	}
  	
	//create a drop-down navigation action bar
	private void setupActionBar(String message){
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		
		final String[] dropdownValues = getResources().getStringArray(R.array.booking_selection);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
		        android.R.layout.simple_spinner_item, android.R.id.text1, dropdownValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.booking_selection,
				android.R.layout.simple_spinner_dropdown_item);
		
		ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener(){   
        	@Override
        	public boolean onNavigationItemSelected(int itemPosition, long itemId){
  	    		
        		switch (itemPosition) {
        		case 0:
        			setupTripBasedBooking();
        			break;
		        case 1:
		        	setupTimeBasedBooking();
		        	break;
	    	    }
        		return true;
        	}
        };
        
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
        if(message.equalsIgnoreCase("tripbased")){
        	actionBar.setSelectedNavigationItem(0);
        }else if(message.equalsIgnoreCase("timebased")){
        	actionBar.setSelectedNavigationItem(1);
        }else{
			Context context = getApplicationContext();
			CharSequence text = "wrong! ";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	//create a tab view
	private void setupTab(){
	    TabSpec ts1 = tabHost.newTabSpec("locationTab");
	    ts1.setIndicator("Step 1: Location");
	    ts1.setContent(R.id.tab1);
	    tabHost.addTab(ts1);
	    tabHost.setId(0);

	    TabSpec ts2 = tabHost.newTabSpec("timeTab");
	    ts2.setIndicator("Step 2: Date & Time");
	    ts2.setContent(R.id.tab2);
	    tabHost.addTab(ts2);
	    tabHost.setId(1);

	    TabSpec ts3 = tabHost.newTabSpec("Tab3");
	    ts3.setIndicator("Step 3: Contact Info");
	    ts3.setContent(R.id.tab3);
	    tabHost.addTab(ts3);
	    tabHost.setId(2);
	}
	
	//list the options of all the tabs
	private void chooseAction(int tabId){
		switch(tabId){
		    case 0:
				//if click switch button
				Switch switchTimeLocationBased = (Switch) findViewById(R.id.switchTimeLocationBased);
				switchAction(switchTimeLocationBased);	    	
				//if click add button
				
				//if click delete button
				break;
		    case 1:
		    	//if click testing button
		    	EditText startTimeEdit = (EditText)findViewById(R.id.startTimeEdit);
		    	showTimePickerDialog(startTimeEdit);	
		    	EditText endTimeEdit = (EditText)findViewById(R.id.endTimeEdit);
		    	showTimePickerDialog (endTimeEdit);  	    		    	
		    	break;
		    case 2:
				//if click submit button
				Button submitButton = (Button) findViewById(R.id.submitButton);
				submitAction(submitButton);
				break;
		}
	}
	   
	//actions if click Switch button
	private void switchAction(Switch switchTimeLocationBased){
		switchTimeLocationBased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    		EditText departureLocReturn = (EditText) findViewById(R.id.departureLocReturn); 
	    		Button deleteButttonReturn = (Button) findViewById(R.id.deleteButttonReturn); 
	    		EditText destinationLocReturn = (EditText) findViewById(R.id.destinationLocReturn); 
	    		Button addButttonReturn = (Button) findViewById(R.id.addButttonReturn); 
	    		TextView endTimeText = (TextView) findViewById(R.id.endTimeText);
	    		EditText endTimeEdit = (EditText) findViewById(R.id.endTimeEdit); 
	    		if(isChecked) {
	    			departureLocReturn.setVisibility(View.VISIBLE);
	    			deleteButttonReturn.setVisibility(View.VISIBLE);
	    			destinationLocReturn.setVisibility(View.VISIBLE);
	    			addButttonReturn.setVisibility(View.VISIBLE);
	    			endTimeText.setVisibility(View.VISIBLE);
	    			endTimeEdit.setVisibility(View.VISIBLE);
	    			
	    	    } else {
	    	    	departureLocReturn.setVisibility(View.GONE);
	    			deleteButttonReturn.setVisibility(View.GONE);
	    			destinationLocReturn.setVisibility(View.GONE);
	    			addButttonReturn.setVisibility(View.GONE);
	    			endTimeText.setVisibility(View.GONE);
	    			endTimeEdit.setVisibility(View.GONE);
	    	    }
	    	}
		});
	}
	
	//actions if click DateTime button
	private void showTimePickerDialog (EditText editBox){
		final EditText editText = editBox;
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		final LayoutInflater layoutInflater = LayoutInflater.from(this);
		editBox.setOnClickListener(new OnClickListener() {
			@Override
	    	public void onClick(View view) {
		    	final View dateView = layoutInflater.inflate(R.layout.date_time_picker, null);
		    	final DatePicker dp = (DatePicker) dateView.findViewById(R.id.datePicker1);
		    	final TimePicker tp = (TimePicker) dateView.findViewById(R.id.timePicker1);
		    	alertDialogBuilder.setView(dateView)
		    		.setTitle("Date & Time")
		    		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int id) {
		    				int year = dp.getYear();
		    				int month = dp.getMonth();
		    				int day = dp.getDayOfMonth();
		    				int hour = tp.getCurrentHour();
		    				int min = tp.getCurrentMinute();
		    				//write into the edittext 
		    				editText.setText(new StringBuilder().append(month + 1)
	    						.append("-").append(day).append("-").append(year).append("  ")
	    						.append(hour).append(":").append(min));		
		    			}
	    	    })
	    	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	    		public void onClick(DialogInterface dialog, int id) {
	    	    			dialog.cancel();
	    	    		}
	    	    });
		    	// create an alert dialog
		    	AlertDialog alertD = alertDialogBuilder.create();
		    	alertD.show();
			}
		});
	}
	
	//actions if click Submit button
	private void submitAction(Button submitButton){
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		final LayoutInflater layoutInflater = LayoutInflater.from(this);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
	    	public void onClick(View view) {
		    	View dateView = layoutInflater.inflate(R.layout.confirmation, null);
		    	alertDialogBuilder.setView(dateView)
		    		.setTitle("Confirmation")
		    		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int id) {
		    				//submit!
		    				Context context = getApplicationContext();
		    				CharSequence text = "You submitted! ";
		    				int duration = Toast.LENGTH_SHORT;
		    				Toast toast = Toast.makeText(context, text, duration);
		    				toast.show();
		    			}
		    		})
	    	    	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	    		public void onClick(DialogInterface dialog, int id) {
	    	    			dialog.cancel();
	    	    		}
	    	    	});
		    	// create an alert dialog
		    	AlertDialog alertD = alertDialogBuilder.create();
		    	alertD.show();
			}
		});
	}
	
	//activate buttons needed for trip-based booking
	private void setupTripBasedBooking(){
  		Switch switchTimeLocationBased= (Switch) findViewById(R.id.switchTimeLocationBased);
		EditText departureLocReturn = (EditText) findViewById(R.id.departureLocReturn); 
		Button deleteButttonReturn = (Button) findViewById(R.id.deleteButttonReturn); 
		EditText destinationLocReturn = (EditText) findViewById(R.id.destinationLocReturn); 
		Button addButttonReturn = (Button) findViewById(R.id.addButttonReturn); 
		TextView endTimeText = (TextView) findViewById(R.id.endTimeText);
		EditText endTimeEdit = (EditText) findViewById(R.id.endTimeEdit); 
		switchTimeLocationBased.setVisibility(View.VISIBLE);
    	if(switchTimeLocationBased.isChecked()== true){
    		departureLocReturn.setVisibility(View.VISIBLE);
    		deleteButttonReturn.setVisibility(View.VISIBLE);
        	destinationLocReturn.setVisibility(View.VISIBLE);
        	addButttonReturn.setVisibility(View.VISIBLE);
        	endTimeText.setVisibility(View.VISIBLE);
        	endTimeEdit.setVisibility(View.VISIBLE);
    	}
	}	
	
	//activate buttons needed for trip-based booking
	private void setupTimeBasedBooking(){
  		Switch switchTimeLocationBased= (Switch) findViewById(R.id.switchTimeLocationBased);
		EditText departureLocReturn = (EditText) findViewById(R.id.departureLocReturn); 
		Button deleteButttonReturn = (Button) findViewById(R.id.deleteButttonReturn); 
		EditText destinationLocReturn = (EditText) findViewById(R.id.destinationLocReturn); 
		Button addButttonReturn = (Button) findViewById(R.id.addButttonReturn); 
		TextView endTimeText = (TextView) findViewById(R.id.endTimeText);
		EditText endTimeEdit = (EditText) findViewById(R.id.endTimeEdit); 
		switchTimeLocationBased.setVisibility(View.GONE);
		departureLocReturn.setVisibility(View.GONE);
		deleteButttonReturn.setVisibility(View.GONE);
    	destinationLocReturn.setVisibility(View.GONE);
    	addButttonReturn.setVisibility(View.GONE);
    	endTimeText.setVisibility(View.GONE);
    	endTimeEdit.setVisibility(View.GONE);
	}
	
}





  
 


