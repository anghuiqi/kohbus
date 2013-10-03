package com.finalyearprojectapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;


public class ProfileActivity extends Activity {
	EditText newName;
	EditText newContact;
	EditText newEmail;
	Button change;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		newName = (EditText)this.findViewById(R.id.newName);
	    newContact = (EditText)this.findViewById(R.id.newContact);
	    newEmail = (EditText)this.findViewById(R.id.newEmail);
	}

	

}