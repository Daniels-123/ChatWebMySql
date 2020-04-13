package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
//import androidx.emoji.bundled.BundledEmojiCompatConfig;
//import androidx.emoji.text.EmojiCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dybcatering.chatwebmysql.usersession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
	private EditText edtemail,edtpass;
	private String email,pass;//,sessionmobile;
	private TextView appname,forgotpass,registernow;
	private RequestQueue requestQueue;
	UserSession userSession;
	private static String URL_LOGIN = "http://192.168.0.13/loginchat/login.php";

	public static final String TAG = "MyTag";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//EmojiCompat.Config config = new BundledEmojiCompatConfig(this)
		//		.setReplaceAll(true);
		//EmojiCompat.init(config);

		setContentView(R.layout.activity_login);

		userSession = new UserSession(this);
		if (userSession.isLoggin()){
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else {


			Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
			appname = findViewById(R.id.appname);
			appname.setTypeface(typeface);

			edtemail = findViewById(R.id.email);
			edtpass = findViewById(R.id.password);

			Bundle registerinfo = getIntent().getExtras();
			if (registerinfo != null) {
				edtemail.setText(registerinfo.getString("email"));
			}


			requestQueue = Volley.newRequestQueue(LoginActivity.this);//Creating the RequestQueue
			registernow = findViewById(R.id.register_now);
			registernow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
					finish();
				}
			});

			Button button = findViewById(R.id.login_button);

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					email = edtemail.getText().toString();

					pass = edtpass.getText().toString();

					//String password = BCrypt.hashpw(pass, BCrypt.gensalt());
					if (!email.isEmpty() || !pass.isEmpty()) {
						Login(email, pass);
					} else {
						edtemail.setError("Por favor inserte su correo");
						edtpass.setError("Por favor inserte su contraseña");


					}

				}
			});
		}
	}

	private void Login(final String email, final String password) {


/*		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							String success = jsonObject.getString("success");
							JSONArray jsonArray = jsonObject.getJSONArray("login");

							if (success.equals("1")) {

								for (int i = 0; i < jsonArray.length(); i++) {

									JSONObject object = jsonArray.getJSONObject(i);

									String name = object.getString("name").trim();
									String email = object.getString("email").trim();
									String id = object.getString("id").trim();
*/
									userSession.createSession("Daniel", email, "7");

									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									intent.putExtra("email", email);
//									intent.putExtra("email", email);
									startActivity(intent);
									finish();
/*
					//				loading.setVisibility(View.GONE);


								}

							}

						} catch (JSONException e) {
							e.printStackTrace();
					//		loading.setVisibility(View.GONE);
					//		btn_login.setVisibility(View.VISIBLE);
							Toast.makeText(LoginActivity.this, "Error " +e.toString(), Toast.LENGTH_SHORT).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					//	loading.setVisibility(View.GONE);
					//	btn_login.setVisibility(View.VISIBLE);
						Toast.makeText(LoginActivity.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("email", email);
				params.put("password", password);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
*/
	}


	private boolean validatePassword(String pass) {


		if (pass.length() < 4 || pass.length() > 20) {
			edtpass.setError("La contraseña debe constar de 4 a 20 caracteres.");
			return false;
		}
		return true;
	}

	private boolean validateUsername(String email) {

		if (email.length() < 4 || email.length() > 50) {
			edtemail.setError("La contraseña debe constar de 4 a 20 caracteres.");
			return false;
		} else if (!email.matches("^[A-za-z0-9.@]+")) {
			edtemail.setError("Solamente . y @ caracteres permitidos");
			return false;
		} else if (!email.contains("@") || !email.contains(".")) {
			edtemail.setError("\n" +
					"El correo electrónico debe contener @ y .");
			return false;
		}
		return true;
	}



}
