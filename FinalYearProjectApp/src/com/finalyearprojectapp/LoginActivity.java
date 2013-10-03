package com.finalyearprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends Activity implements OnClickListener{
	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnCancel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
       
		txtUserName=(EditText)this.findViewById(R.id.txtUname);
		txtPassword=(EditText)this.findViewById(R.id.txtPwd);
		btnLogin=(Button)this.findViewById(R.id.btnLogin);
		//btnLogin=(Button)this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);  
	}
   
	@Override
    public void onClick(View v) {
		if((txtUserName.getText().toString()).equals("admin") && (txtPassword.getText().toString().equals("admin"))){
			//Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_LONG).show();
			Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
			startActivity(i);
		}else{
			Toast.makeText(LoginActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
		}
  	}
}

