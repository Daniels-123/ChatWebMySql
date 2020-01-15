package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dybcatering.chatwebmysql.usersession.UserSession;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

	private UserSession session;
	private HashMap<String, String> user;
	private String name, email, photo, mobile;
	private String first_time;

	UserSession sessionManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sessionManager = new UserSession(this);
		sessionManager.checkLogin();

		//Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
		//TextView appname = findViewById(R.id.appname);
		//appname.setTypeface(typeface);


		//getValues();


		//	if (session.getFirstTime()) {
		//		//tap target view
		//		Toast.makeText(this, "Primera vez", Toast.LENGTH_SHORT).show();
		//		session.setFirstTime(false);
		//	}
		//}


/*	private void getValues() {

		//create new session object by passing application context
	//	session = new UserSession(getApplicationContext());

		//validating session
		session.isLoggedIn();

		//get User details if logged in
		user = session.getUserDetails();

		name = user.get(UserSession.KEY_NAME);
		email = user.get(UserSession.KEY_EMAIL);
		mobile = user.get(UserSession.KEY_MOBiLE);
		photo = user.get(UserSession.KEY_PHOTO);
	}
*/
	}
}
