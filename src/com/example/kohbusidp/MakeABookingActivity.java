package com.kohbus;

import java.util.ArrayList;

import android.app.*;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.OnNavigationListener;
import android.content.*;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.kohbus.SessionManagement;

@SuppressWarnings("deprecation")
public class MakeABookingActivity extends TabActivity{
/*	private SessionManagement session;*/
	
	private TabHost tabHost;
	private ActionBar actionBar;
		
	private Button addLoc;
	private Button addLocReturn;
	
	private Switch switchTimeLocationBased;
	
	private LinearLayout moreLocationReturnll;
	private LinearLayout moreLocationll;
	
	private TextView startTimeText;
	private TextView endTimeText;
	private EditText startTimeEdit;	
	private EditText endTimeEdit;
	private EditText passengersTextBox;
	private EditText departureLoc;
	private EditText destinationLoc;
	private EditText departureLocReturn;
	private EditText destinationLocReturn;
	
	private CheckBox contactPersonCheck;
	private EditText name;
	private EditText phone;
	private EditText email;
	private EditText comment;
	private Button submitButton;
	
	private String convertedName;
	private String convertedPhone;
	private String convertedEmail;
	private String convertedComment;
	private String convertedPassengersNo;
	private String convertedStartTime;
	private String convertedEndTime;
	private String convertedDepartureLoc;
	private String convertedDestinationLoc;
	private String convertedDepartureLocReturn;
	private String convertedDestinationLocReturn;
	
	private View dateView;
	
	//final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	//final LayoutInflater layoutInflater = LayoutInflater.from(this);
	
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		
		tabHost = getTabHost();
		
		passengersTextBox = (EditText) findViewById(R.id.passengersTextBox);
		departureLoc = (EditText) findViewById(R.id.departureLoc);
		destinationLoc =(EditText) findViewById(R.id.destinationLoc);
		departureLocReturn = (EditText) findViewById(R.id.departureLocReturn); 
		destinationLocReturn = (EditText) findViewById(R.id.destinationLocReturn); 
		startTimeEdit = (EditText) findViewById(R.id.startTimeEdit);
		endTimeEdit = (EditText) findViewById(R.id.endTimeEdit); 
		name = (EditText) findViewById(R.id.name);
		phone =  (EditText) findViewById(R.id.phone);
		email =  (EditText) findViewById(R.id.email);
		comment =  (EditText) findViewById(R.id.comment);
		
		startTimeText = (TextView) findViewById(R.id.startTimeText);		
		endTimeText = (TextView) findViewById(R.id.endTimeText);
		
		moreLocationll =(LinearLayout) findViewById(R.id.moreLocationll);
		moreLocationReturnll = (LinearLayout) findViewById(R.id.moreLocationReturnll);
		
	
		addLoc = (Button) findViewById(R.id.addLoc);
		addLocReturn =(Button) findViewById(R.id.addLocReturn);
		submitButton =  (Button) findViewById(R.id.submitButton);
		
		switchTimeLocationBased= (Switch) findViewById(R.id.switchTimeLocationBased);
		
		contactPersonCheck = (CheckBox) findViewById(R.id.contactPersonCheck);
		
		
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
		
		actionBar = getActionBar();
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
				switchAction(switchTimeLocationBased);	  
				//if click add location button
				addLocation(addLoc);
				//if click add location button for return trip
				addLocation(addLocReturn);
				break;
		    case 1:
		    	//if click testing button
		    	showTimePickerDialog(startTimeEdit);	
		    	showTimePickerDialog (endTimeEdit);  	    		    	
		    	break;
		    case 2:
				//if click submit button
				submitAction(submitButton);
				break;
		}
	}
	 
	
	//actions if click Switch button
	private void switchAction(Switch switchTimeLocationBased){
		switchTimeLocationBased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    		if(isChecked) {
	    			setStatus(View.VISIBLE);
	    	    } else {
	    	    	setStatus(View.GONE);
	    	    }
	    	}
		});
	}
	
	
	//actions if click add location button
	private void addLocation (Button addLoc){
		final Context myContext =getBaseContext();
		final int id = addLoc.getId();
		addLoc.setOnClickListener(new OnClickListener() {
			@Override
	    	public void onClick(View view) {
				TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(60, 10, 60, 0);
				LinearLayout mLinearLayout;
				if(id == 0x7f09004f){
					mLinearLayout = moreLocationll; //(LinearLayout)findViewById(R.id.moreLocationll);
				}else {
					mLinearLayout = moreLocationReturnll; //(LinearLayout)findViewById(R.id.moreLocationReturnll);
				}
				
				EditText moreLoc = new EditText(myContext);
				moreLoc.setHint("Text here");
				moreLoc.setBackgroundResource(R.drawable.rounded_edittext);
				moreLoc.setLayoutParams(params);
				mLinearLayout.addView(moreLoc);
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
		    	
		    	alertDialogBuilder.setView(dateView).setTitle("Date & Time").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
							
				
				if(verifyFields() == true){
					//Log.v("dalvikvm", "works");
					
					dateView = layoutInflater.inflate(R.layout.confirmation,null);
					
					//Log.v("dalvikvm",  passengersTextBox.getText().toString());
					confirmation();
										
					alertDialogBuilder.setView(dateView).setTitle("Confirmation")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    		
							
			    		public void onClick(DialogInterface dialog, int id) {
			    				//submit!
			    			
			    			Context context = getApplicationContext();
		    				CharSequence text = "You submitted! ";
		    				int duration = Toast.LENGTH_SHORT;
		    				Toast toast = Toast.makeText(context, text, duration);
		    				toast.show();
		    			}
	    			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    	    		
			    				public void onClick(DialogInterface dialog, int id) {
		    	    			dialog.cancel();
		    	    		}
		    	    	});
			    	// create an alert dialog
			    	AlertDialog alertD = alertDialogBuilder.create();
			    	alertD.show();
					}/*else{
						Log.v("dalvikvm", "not works");
					}
					*/
				}
		});
	}
	
	
	//activate buttons needed for trip-based booking
	private void setupTripBasedBooking(){
		switchTimeLocationBased.setVisibility(View.VISIBLE);
    	if(switchTimeLocationBased.isChecked()== true){
    		setStatus(View.VISIBLE); 	
    	}
	}	
	
	
	//activate buttons needed for time-based booking
	private void setupTimeBasedBooking(){
		switchTimeLocationBased.setVisibility(View.GONE);
		setStatus(View.GONE);
	}
	
	private void setStatus(int visibility){
		departureLocReturn.setVisibility(visibility);
		addLocReturn.setVisibility(visibility);
		destinationLocReturn.setVisibility(visibility);
		endTimeText.setVisibility(visibility);
		endTimeEdit.setVisibility(visibility);
		moreLocationReturnll.setVisibility(visibility);
	}
	
	
	private boolean verifyFields(){
		
		boolean validation = true;
		
		passengersTextBox.setError(null);
		departureLoc.setError(null);
		destinationLoc.setError(null);
		startTimeEdit.setError(null);
		endTimeEdit.setError(null);
		name.setError(null);
		phone.setError(null);
		email.setError(null);
		departureLocReturn.setError(null);
		destinationLocReturn.setError(null);
		
		View focusView = null;
				
		//need to add one more validation: if checkbox is ticked, convertedName = sessionUser
		convertedName = name.getText().toString();
		convertedPhone = phone.getText().toString();
		convertedEmail = email.getText().toString();
		
		convertedPassengersNo = passengersTextBox.getText().toString(); 
		convertedStartTime = startTimeEdit.getText().toString();
		convertedEndTime = endTimeEdit.getText().toString();
		
		convertedDepartureLoc = departureLoc.getText().toString();;
		convertedDestinationLoc = destinationLoc.getText().toString();;
		
		convertedDepartureLocReturn = departureLocReturn.getText().toString();
		convertedDestinationLocReturn = destinationLocReturn.getText().toString();
		
		if (TextUtils.isEmpty(convertedName)) {
			name.setError(getString(R.string.error_field_required));
			focusView = name;
			validation= false;
		}
		
		if (TextUtils.isEmpty(convertedPhone)) {
			phone.setError(getString(R.string.error_field_required));
			focusView = phone;
			validation = false;
		}
		
		if (TextUtils.isEmpty(convertedEmail)) {
			email.setError(getString(R.string.error_field_required));
			focusView = email;
			validation = false;
		}
				
		if (TextUtils.isEmpty(convertedPassengersNo)) {
			passengersTextBox.setError(getString(R.string.error_field_required));
			focusView = passengersTextBox;
			validation = false;
		}
		
		
		if (TextUtils.isEmpty(convertedDepartureLoc)) {
			departureLoc.setError(getString(R.string.error_field_required));
			focusView = departureLoc;
			validation = false;
		}
		
		if (TextUtils.isEmpty(convertedDestinationLoc)) {
			destinationLoc.setError(getString(R.string.error_field_required));
			focusView = destinationLoc;
			validation = false;
		}
		
		
		
		if (TextUtils.isEmpty(convertedStartTime)) {
			startTimeEdit.setError(getString(R.string.error_field_required));
			focusView = startTimeEdit;
			validation = false;
		}
		
		//only implement this if round trip is select
		
		if(switchTimeLocationBased.isChecked()== true){
			if (TextUtils.isEmpty(convertedEndTime)) {
				endTimeEdit.setError(getString(R.string.error_field_required));
				focusView = endTimeEdit;
				validation = false;
			}
			
			if (TextUtils.isEmpty(convertedDestinationLocReturn)) {
				destinationLocReturn.setError(getString(R.string.error_field_required));
				focusView = destinationLocReturn;
				validation = false;
			}
			
			if (TextUtils.isEmpty(convertedDestinationLocReturn)) {
				destinationLocReturn.setError(getString(R.string.error_field_required));
				focusView = destinationLocReturn;
				validation = false;
			}
		}
				
		return validation;
	}
	
	
	public void confirmation(){
		
		//dateView = layoutInflater.inflate(R.layout.confirmation,null);		
		TextView confirmedPassenger = (TextView) dateView.findViewById(R.id.noPassengerValue);
		TextView confirmedDepartLoc = (TextView) dateView.findViewById(R.id.departLocValue);
		TextView confirmedDestLoc = (TextView) dateView.findViewById(R.id.destLocValue);
		TextView confirmedStartDT = (TextView) dateView.findViewById(R.id.confirmedStartDT);
		TextView confirmedName = (TextView) dateView.findViewById(R.id.confirmedName);
		TextView confirmedNumber = (TextView) dateView.findViewById(R.id.confirmedNumber);
		TextView confirmedEmail = (TextView) dateView.findViewById(R.id.confirmedEmail);
		TextView confirmedComments = (TextView) dateView.findViewById(R.id.confirmedComments);
			
		confirmedPassenger.setText(passengersTextBox.getText());
		confirmedDepartLoc.setText(departureLoc.getText());
		confirmedDestLoc.setText(destinationLoc.getText());
		confirmedStartDT.setText(startTimeEdit.getText());
		confirmedName.setText(name.getText());
		confirmedNumber.setText(phone.getText());
		confirmedEmail.setText(email.getText());
		confirmedComments.setText(comment.getText());
		
	}
}





  
 


