package com.example.kohbusidp;

import java.util.*;
import java.util.regex.Pattern;

import android.app.*;
import android.app.ActionBar.LayoutParams;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;


@SuppressWarnings("deprecation")
public class MakeABookingActivity extends TabActivity{
/*	private SessionManagement session;*/
	private TabHost tabHost;
	private ActionBar actionBar;
		
	private Button addLoc;
	private Button addLocReturn;
	private Button submitButton;
	private TableLayout tLayout;
	private TableLayout tLayoutReturn;
	private Switch switchTimeLocationBased;
	private CheckBox contactPersonCheck;
	private ProgressDialog pdialog;
	
	//below is to get value from the make_booking.xml
	private EditText startTimeEdit;	
	private TextView startTimeReturnText;//optional
	private EditText startTimeReturnEdit;//optional
	private EditText passengersTextBox;
	private AutoCompleteTextView departureLoc;
	private AutoCompleteTextView destinationLoc;
	private AutoCompleteTextView departureLocReturn;//optional
	private AutoCompleteTextView destinationLocReturn;//optional
	private EditText name;
	private EditText phone;
	private EditText email;
	private EditText comment;//optional
	
	//below is to convert values from EditText to String
	private String convertedName;
	private String convertedPhone;
	private String convertedEmail;
	private String convertedComment;//optional
	private String convertedPassengersNo;
	private String convertedDepartureLoc;
	private String convertedDestinationLoc;
	private String convertedDepartureLocReturn;//optional
	private String convertedDestinationLocReturn;//optional
	private String convertedStartTime;
	private String convertedStartTimeReturn;//optional
	private String convertedStopovers;//optional
	private String convertedStopoversReturn;//optional
	
	private View confirmView;
	
	private Calendar c;
	
	//final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	//final LayoutInflater layoutInflater = LayoutInflater.from(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_booking);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tabHost = getTabHost();
			
		pdialog = new ProgressDialog(this);	
		
		//This is an example of how to use auto-complete place
		departureLoc = (AutoCompleteTextView) findViewById(R.id.departureLoc);
		destinationLoc =(AutoCompleteTextView) findViewById(R.id.destinationLoc);
		departureLocReturn = (AutoCompleteTextView) findViewById(R.id.departureLocReturn);//optional
		destinationLocReturn = (AutoCompleteTextView) findViewById(R.id.destinationLocReturn);//optional
		
		departureLoc.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));
		destinationLoc.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));
		departureLocReturn.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));//optional
		destinationLocReturn.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));//optional
		
		passengersTextBox = (EditText) findViewById(R.id.passengersTextBox);
		startTimeEdit = (EditText) findViewById(R.id.startTimeEdit);
		startTimeReturnEdit = (EditText) findViewById(R.id.startTimeReturnEdit); //optional
		name = (EditText) findViewById(R.id.name);
		phone =  (EditText) findViewById(R.id.phone);
		email =  (EditText) findViewById(R.id.email);
		comment =  (EditText) findViewById(R.id.comment);//optionals
		
		startTimeReturnText = (TextView) findViewById(R.id.startTimeReturnText);//optional
		
		tLayout = (TableLayout) findViewById(R.id.tLayout);
		tLayoutReturn = (TableLayout) findViewById(R.id.tLayoutReturn);

		
		addLoc = (Button) findViewById(R.id.addLoc);
		addLocReturn =(Button) findViewById(R.id.addLocReturn);//optional
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
	    ts1.setIndicator(getResources().getString(R.string.step1));
	    ts1.setContent(R.id.tab1);
	    tabHost.addTab(ts1);
	    tabHost.setId(0);

	    TabSpec ts2 = tabHost.newTabSpec("timeTab");
	    ts2.setIndicator(getResources().getString(R.string.step2));
	    ts2.setContent(R.id.tab2);
	    tabHost.addTab(ts2);
	    tabHost.setId(1);

	    TabSpec ts3 = tabHost.newTabSpec("Tab3");
	    ts3.setIndicator(getResources().getString(R.string.step3));
	    ts3.setContent(R.id.tab3);
	    tabHost.addTab(ts3);
	    tabHost.setId(2);
	}
	
	
	//list the options of all the tabs
	private void chooseAction(int tabId){
		switch(tabId){
		    case 0:
				switchAction(switchTimeLocationBased);	  
				addLocation(addLoc);
				addLocation(addLocReturn);
				break;
		    case 1:
		    	showTimePickerDialog(startTimeEdit);	
		    	showTimePickerDialog (startTimeReturnEdit);  	    		    	
		    	break;
		    case 2:
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
	private void addLocation (Button stopover){
		final int id = stopover.getId();
		
		stopover.setOnClickListener(new OnClickListener() {
			@Override
	    	public void onClick(View view) {
				final TableRow row = new TableRow(getApplicationContext());
				row.setLayoutParams(new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.WRAP_CONTENT,10f));
		        
				Drawable remove = getResources().getDrawable(R.drawable.ic_action_remove);
				
		        Button deleteButton  = new Button(getApplicationContext(), null, android.R.attr.buttonStyleSmall);
		        deleteButton.setBackgroundDrawable(remove);
				
		        AutoCompleteTextView oneMoreLoc = new AutoCompleteTextView(getApplicationContext());
				oneMoreLoc.setLayoutParams(new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.WRAP_CONTENT,8.5f));
				oneMoreLoc.setHint(R.string.stopovers);
				oneMoreLoc.setBackgroundResource(R.layout.templete_edittext);
				oneMoreLoc.setTextColor(Color.BLACK);
				oneMoreLoc.setAdapter(new PlacesAutoCompleteAdapter(MakeABookingActivity.this, R.layout.item_list));
								
				//set the specific place for stop-over
				if(id == R.id.addLoc){
					row.addView(deleteButton);
					row.addView(oneMoreLoc);
					tLayout.addView(row,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				}else {
					row.addView(deleteButton);
					row.addView(oneMoreLoc); 
					tLayoutReturn.addView(row,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				}
				
				
				//if click delete button
				deleteButton.setOnClickListener(new OnClickListener() {
					@Override
			    	public void onClick(View view) {
						if(id == R.id.addLoc){
							tLayout.removeView(row);
						}else {
							tLayoutReturn.removeView(row);
						}
					}
				});			
				
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
									
				if(verifyFields() == true){
					confirmView = layoutInflater.inflate(R.layout.confirmation,null);
					
					showConfirmation();
										
					alertDialogBuilder.setView(confirmView).setTitle("Confirmation")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int id) {
				    			//submit!
				    			Context context = getApplicationContext();
								CharSequence text = "You submit successfully!" ;
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
				
				}else{
					Context context = getApplicationContext();
					CharSequence text = getResources().getString(R.string.failSubmit);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
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
		startTimeReturnText.setVisibility(View.VISIBLE);
		startTimeReturnEdit.setVisibility(View.VISIBLE);
	}
	
	private void setStatus(int visibility){
		departureLocReturn.setVisibility(visibility);
		addLocReturn.setVisibility(visibility);
		destinationLocReturn.setVisibility(visibility);
		tLayoutReturn.setVisibility(visibility);
		startTimeReturnText.setVisibility(visibility);
		startTimeReturnEdit.setVisibility(visibility);
	}
	
	
	private boolean verifyFields(){
		boolean validation = true;
		
		passengersTextBox.setError(null);
		departureLoc.setError(null);
		destinationLoc.setError(null);
		startTimeEdit.setError(null);
		startTimeReturnEdit.setError(null);
		name.setError(null);
		phone.setError(null);
		email.setError(null);
		departureLocReturn.setError(null);
		destinationLocReturn.setError(null);
		
		View focusView = null;
		//need to add one more validation: if checkbox is ticked, convertedName = sessionUser
		convertedPassengersNo = passengersTextBox.getText().toString(); 

		convertedDepartureLoc = departureLoc.getText().toString();
		convertedDestinationLoc = destinationLoc.getText().toString();
		convertedStopovers = "";//optional
		
		convertedDepartureLocReturn = departureLocReturn.getText().toString();//optional
		convertedDestinationLocReturn = destinationLocReturn.getText().toString();//optional
		convertedStopoversReturn = "";//optional
		
		convertedStartTime = startTimeEdit.getText().toString();
		convertedStartTimeReturn = startTimeReturnEdit.getText().toString();//optional

		convertedName = name.getText().toString();
		convertedPhone = phone.getText().toString();
		convertedEmail = email.getText().toString();
		convertedComment = comment.getText().toString();
		
		if (TextUtils.isEmpty(convertedName)) {
			name.setError(getString(R.string.error_field_required));
			focusView = name;
			validation= false;
		}
		
		if (TextUtils.isEmpty(convertedPhone)) {
			phone.setError(getString(R.string.error_field_required));
			focusView = phone;
			validation = false;
		} else {
			//validating that the phone number has a total of 8 numbers and it begins with 6,8 or 9
			//String firstNumber = convertedPhone.substring(0,1);
			if(convertedPhone.length() != 8){
				phone.setError(getString(R.string.error_invalid_contact));
				focusView = phone;
				validation = false;
			}else if(convertedPhone.length() == 8){
				Pattern p = Pattern.compile("[0-9]*");
				if(!p.matcher(convertedPhone).matches()){
					phone.setError(getString(R.string.error_invalid_contact));
					focusView = phone;
					validation = false;
				}				
			}else{
				focusView = phone;
				validation = false;
			}
		}
		
		if (TextUtils.isEmpty(convertedEmail)) {
			email.setError(getString(R.string.error_field_required));
			focusView = email;
			validation = false;
		} else if (!convertedEmail.contains("@")) {
			email.setError(getString(R.string.error_invalid_email));
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
		
		//check & add the ArrayList
		int noOfTableRow;
		int noOfTableRowReturn;
		try{
			noOfTableRow = tLayout.getChildCount();
			noOfTableRowReturn = tLayoutReturn.getChildCount();
		}catch (Exception e){
			noOfTableRow = 0;
			noOfTableRowReturn = 0;
		}
		
		if(noOfTableRow != 0){
			loop:
			for(int i = 0; i < noOfTableRow; i++){
				TableRow row = (TableRow) tLayout.getChildAt(i);
				AutoCompleteTextView tv = (AutoCompleteTextView) row.getChildAt(1);
				String location =tv.getText().toString();
				if(location.equals("")){
					validation = false;
					break loop;
				}
			}
			//add to ArrayList
			for(int i =0; i < noOfTableRow; i++){
				TableRow row = (TableRow) tLayout.getChildAt(i);
				AutoCompleteTextView tv = (AutoCompleteTextView) row.getChildAt(1);
				convertedStopovers = convertedStopovers + tv.getText().toString() + "; ";
			}
		}


		if(switchTimeLocationBased.isChecked()== true){
			if (TextUtils.isEmpty(convertedStartTimeReturn)) {
				startTimeReturnEdit.setError(getString(R.string.error_field_required));
				focusView = startTimeReturnEdit;
				validation = false;
			}
			
			if (TextUtils.isEmpty(convertedDepartureLocReturn)) {
				destinationLocReturn.setError(getString(R.string.error_field_required));
				focusView = destinationLocReturn;
				validation = false;
			}
			
			if (TextUtils.isEmpty(convertedDestinationLocReturn)) {
				destinationLocReturn.setError(getString(R.string.error_field_required));
				focusView = destinationLocReturn;
				validation = false;
			}
			
			//add and check the arraylist
			//only implement this if round trip is select
			if(noOfTableRowReturn != 0){
				loop:
				for(int i = 0; i < noOfTableRowReturn; i++){
					TableRow row = (TableRow) tLayoutReturn.getChildAt(i);
					AutoCompleteTextView tv = (AutoCompleteTextView) row.getChildAt(1);
					String location =tv.getText().toString();
					if(location.equals("")){
						validation = false;
						break loop;
					}
				}
				//add to ArrayList
				for(int i =0; i < noOfTableRowReturn; i++){
					TableRow row = (TableRow) tLayoutReturn.getChildAt(i);
					AutoCompleteTextView tv = (AutoCompleteTextView) row.getChildAt(1);
					convertedStopoversReturn = convertedStopoversReturn + tv.getText().toString() + "; ";
				}
			}
		}
				
		return validation;
	}
	
	
	public void showConfirmation(){
		
		TextView confirmedPassenger = (TextView) confirmView.findViewById(R.id.noPassengerValue);
		TextView confirmedDepartLoc = (TextView) confirmView.findViewById(R.id.departLocValue);
		TextView confirmedStopoverList = (TextView) confirmView.findViewById(R.id.confirmedStopoverList);//optional
		TextView confirmedDestLoc = (TextView) confirmView.findViewById(R.id.destLocValue);
		TextView confirmDepartLocReturn = (TextView) confirmView.findViewById(R.id.departLocValueReturn);//optional
		TextView confirmedStopoverListReturn = (TextView) confirmView.findViewById(R.id.confirmedStopoverListReturn);//optional
		TextView confirmDestLocReturn = (TextView) confirmView.findViewById(R.id.destLocValueReturn);//optional
		TextView confirmedStartDT = (TextView) confirmView.findViewById(R.id.confirmedStartDT);
		TextView confirmedStartDTReturn = (TextView) confirmView.findViewById(R.id.confirmedStartDTReturn);//optional
		TextView confirmedName = (TextView) confirmView.findViewById(R.id.confirmedName);
		TextView confirmedNumber = (TextView) confirmView.findViewById(R.id.confirmedNumber);
		TextView confirmedEmail = (TextView) confirmView.findViewById(R.id.confirmedEmail);
		TextView confirmedComments = (TextView) confirmView.findViewById(R.id.confirmedComments);//optional
		
		TextView confirmedCommentText = (TextView) confirmView.findViewById(R.id.comments);//optional
		TextView textStopoverList = (TextView) confirmView.findViewById(R.id.stopoverList);//optional text
		TextView textdepartLocReturn = (TextView) confirmView.findViewById(R.id.departLocReturn);//optional text
		TextView textStopoverListReturn = (TextView) confirmView.findViewById(R.id.stopoverListReturn);//optional text
		TextView textDestLocReturn = (TextView) confirmView.findViewById(R.id.destLocReturn);//optional text
		TextView textStartDTReturn = (TextView) confirmView.findViewById(R.id.startDTReturn);//optional text
		
		confirmedPassenger.setText(passengersTextBox.getText());
		confirmedDepartLoc.setText(departureLoc.getText());
		confirmedDestLoc.setText(destinationLoc.getText());
		confirmedStartDT.setText(startTimeEdit.getText());
		confirmedName.setText(name.getText());
		confirmedNumber.setText(phone.getText());
		confirmedEmail.setText(email.getText());
		
		if(!convertedComment.equals("")){
			confirmedCommentText.setVisibility(View.VISIBLE);
			confirmedComments.setVisibility(View.VISIBLE);
			confirmedComments.setText(comment.getText());
		}
		
		if(!convertedStopovers.equals("")){
			textStopoverList.setVisibility(View.VISIBLE);
			confirmedStopoverList.setVisibility(View.VISIBLE);
			confirmedStopoverList.setText(convertedStopovers);
		}
		
		if(switchTimeLocationBased.isChecked() == true){
			textdepartLocReturn.setVisibility(View.VISIBLE);
			textDestLocReturn.setVisibility(View.VISIBLE);
			textStartDTReturn.setVisibility(View.VISIBLE);
			
			confirmDepartLocReturn.setVisibility(View.VISIBLE);
			confirmDestLocReturn.setVisibility(View.VISIBLE);
			confirmedStartDTReturn.setVisibility(View.VISIBLE);
			
			confirmDepartLocReturn.setText(departureLocReturn.getText());
			confirmDestLocReturn.setText(destinationLocReturn.getText());
			confirmedStartDTReturn.setText(startTimeReturnEdit.getText());
			
			if(!convertedStopoversReturn.equals("")){
				textStopoverListReturn.setVisibility(View.VISIBLE);
				confirmedStopoverListReturn.setVisibility(View.VISIBLE);
				confirmedStopoverListReturn.setText(convertedStopoversReturn);
			}
		}
	}
	
	//method for comparing time
	public boolean verifyTime(String startTime){
		return true;
	}
	
}






  
 


