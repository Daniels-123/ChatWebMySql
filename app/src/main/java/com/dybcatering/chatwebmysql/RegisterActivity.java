package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
/*import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
*/
import org.json.JSONException;
import org.json.JSONObject;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

	private EditText edtname, edtemail, edtpass, edtcnfpass, edtnumber;
	private String pass;
	private Button button;
	CircleImageView image;
	ImageView upload;
	RequestQueue requestQueue;
	//boolean IMAGE_STATUS = false;
	Bitmap profilePicture;
	public static final String TAG = "MyTag";
	private static String URL_REGIST = "http://192.168.0.13/loginchat/register.php";
	private ProgressBar loading;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);


		edtname = findViewById(R.id.name);
		edtemail = findViewById(R.id.email);
		edtpass = findViewById(R.id.password);
		edtcnfpass = findViewById(R.id.confirmpassword);
		edtnumber = findViewById(R.id.number);

		button=findViewById(R.id.register);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Regist(edtname.getText().toString(), edtemail.getText().toString(), edtpass.getText().toString());
			}
		});

		//Take already registered user to login page

		final TextView loginuser=findViewById(R.id.login_now);
		loginuser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
				finish();
			}
		});

	}


	private void Regist(final String nombre, final String email, final String password){


		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try{
							JSONObject jsonObject = new JSONObject(response);
							String success = jsonObject.getString("success");

							if (success.equals("1")) {
								Toast.makeText(RegisterActivity.this, "Se ha registrado con exito!", Toast.LENGTH_SHORT).show();
							}


						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(RegisterActivity.this, "Se ha producido un error en el registro! " + e.toString(), Toast.LENGTH_SHORT).show();
							loading.setVisibility(View.GONE);
							button.setVisibility(View.VISIBLE);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(RegisterActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
						loading.setVisibility(View.GONE);
						button.setVisibility(View.VISIBLE);
					}
				})

		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("name", nombre);
				params.put("email", email);
				params.put("password", password);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);


	}
}
