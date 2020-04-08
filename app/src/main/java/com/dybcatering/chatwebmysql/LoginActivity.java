package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.request.CheckInternetConnection;
import com.dybcatering.chatwebmysql.request.LoginRequest;
import com.dybcatering.chatwebmysql.usersession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
	private EditText edtemail,edtpass;
	private String email,pass;//,sessionmobile;
	private TextView appname,forgotpass,registernow;
	private RequestQueue requestQueue;
	UserSession userSession;

	public static final String TAG = "MyTag";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//EmojiCompat.Config config = new BundledEmojiCompatConfig(this)
		//		.setReplaceAll(true);
		//EmojiCompat.init(config);

		setContentView(R.layout.activity_login);

		new CheckInternetConnection(this).checkConnection();
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

					if (validateUsername(email) && validatePassword(pass)) { //Username and Password Validation

						//Progress Bar while connection establishes

						final KProgressHUD progressDialog = KProgressHUD.create(LoginActivity.this)
								.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
								.setLabel("Please wait")
								.setCancellable(false)
								.setAnimationSpeed(2)
								.setDimAmount(0.5f)
								.show();


						LoginRequest loginRequest = new LoginRequest(email, pass, new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {

								progressDialog.dismiss();
								// Response from the server is in the form if a JSON, so we need a JSON Object
								try {
									JSONObject jsonObject = new JSONObject(response);
									if (jsonObject.getBoolean("success")) {

										//Passing all received data from server to next activity
										String sessionname = jsonObject.getString("name");
										//sessionmobile = jsonObject.getString("mobile");
										String sessionemail = jsonObject.getString("email");
										//	String sessionphoto =  jsonObject.getString("url");

										//create shared preference and store data
										userSession.createSession(sessionname, sessionemail);

										//count value of firebase cart and wishlist
										//countFirebaseValues();

										Intent loginSuccess = new Intent(LoginActivity.this, MainActivity.class);
										startActivity(loginSuccess);
										finish();
									} else {
										if (jsonObject.getString("status").equals("INVALID"))
											Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
										else {
											Toast.makeText(LoginActivity.this, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
									Toast.makeText(LoginActivity.this, "Error del servidor - Respuesta", Toast.LENGTH_SHORT).show();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								progressDialog.dismiss();
								if (error instanceof ServerError)
									Toast.makeText(LoginActivity.this, "Error del servidor", Toast.LENGTH_SHORT).show();
								else if (error instanceof TimeoutError)
									Toast.makeText(LoginActivity.this, "Tiempo de conexión agotado", Toast.LENGTH_SHORT).show();
								else if (error instanceof NetworkError)
									Toast.makeText(LoginActivity.this, "Mala conexión de red", Toast.LENGTH_SHORT).show();
							}
						});
						loginRequest.setTag(TAG);
						requestQueue.add(loginRequest);
					}

				}
			});
		}
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
