package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.dybcatering.chatwebmysql.request.RegisterRequest;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

	private EditText edtname, edtemail, edtpass, edtcnfpass, edtnumber;
	private String check,name,email,password,username,profile;
	private String pass;
	CircleImageView image;
	ImageView upload;
	RequestQueue requestQueue;
	//boolean IMAGE_STATUS = false;
	Bitmap profilePicture;
	public static final String TAG = "MyTag";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
		TextView appname = findViewById(R.id.appname);
		appname.setTypeface(typeface);

		//upload=findViewById(R.id.uploadpic);
		//image=findViewById(R.id.profilepic);
		edtname = findViewById(R.id.name);
		edtemail = findViewById(R.id.email);
		edtpass = findViewById(R.id.password);
		edtcnfpass = findViewById(R.id.confirmpassword);
		edtnumber = findViewById(R.id.number);

		edtname.addTextChangedListener(nameWatcher);
		edtemail.addTextChangedListener(emailWatcher);
		edtpass.addTextChangedListener(passWatcher);
		edtcnfpass.addTextChangedListener(cnfpassWatcher);
		edtnumber.addTextChangedListener(numberWatcher);

		requestQueue = Volley.newRequestQueue(RegisterActivity.this);

		Button button=findViewById(R.id.register);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			//if (validateProfile() &&
					if (validateName() && validateEmail() && validatePass() && validateCnfPass() && validateNumber()){

					name=edtname.getText().toString();
					email=edtemail.getText().toString();
					password=edtcnfpass.getText().toString();
					//pass = BCrypt.hashpw(password, BCrypt.gensalt());
					username=edtnumber.getText().toString();


					final KProgressHUD progressDialog=  KProgressHUD.create(RegisterActivity.this)
							.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
							.setLabel("Por Favor espera")
							.setCancellable(false)
							.setAnimationSpeed(2)
							.setDimAmount(0.5f)
							.show();


					//Validation Success
					//convertBitmapToString(profilePicture);
					RegisterRequest registerRequest = new RegisterRequest(name, email, username, password, new Response.Listener<String>() {
						@Override
					 	public void onResponse(String response) {
							progressDialog.dismiss();

							Log.e("Response from server", response);

							try {
								if (new JSONObject(response).getBoolean("success")) {

									Toast.makeText(RegisterActivity.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();

									sendRegistrationEmail(name,email);


								} else
									Toast.makeText(RegisterActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();

							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(RegisterActivity.this, "Falló la conexión al registrarse", Toast.LENGTH_SHORT).show();
							}
						}
					});
					requestQueue.add(registerRequest);
				}
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
	/*	upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {

				Dexter.withActivity(RegisterActivity.this)
						.withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
								android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
						.withListener(new MultiplePermissionsListener() {
							@Override
							public void onPermissionsChecked(MultiplePermissionsReport report) {
								// check if all permissions are granted
								if (report.areAllPermissionsGranted()) {
									// do you work now
									Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
									intent.setType("image/*");
									startActivityForResult(intent, 1000);
								}

								// check for permanent denial of any permission
								if (report.isAnyPermissionPermanentlyDenied()) {
									// permission is denied permenantly, navigate user to app settings
									Snackbar.make(view, "Por favor otorgue el permiso requerido", Snackbar.LENGTH_LONG)
											.setAction("Permitir", null).show();
								}
							}

							@Override
							public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
								token.continuePermissionRequest();
							}
						})
						.onSameThread()
						.check();



				//result will be available in onActivityResult which is overridden
			}
		});*/
	}
	private void sendRegistrationEmail(final String name, final String emails) {


		BackgroundMail.newBuilder(RegisterActivity.this)
				.withSendingMessage("Envío de saludos de bienvenida a su correo electrónico !")
				.withSendingMessageSuccess("Compruebe amablemente su correo electrónico ahora!")
				.withSendingMessageError("¡Error al enviar la contraseña! Inténtalo de nuevo !")
				.withUsername("prueba.correo7521@gmail.com")
				.withPassword("N1m2g3e4r57791")
				.withMailto(emails)
				.withType(BackgroundMail.TYPE_PLAIN)
				.withSubject("Saludos desde la aplicación")
				.withBody("Hola Mr/Miss, "+ name + "\n " + getString(R.string.registermail1))
				.send();



	}
	//private void convertBitmapToString(Bitmap profilePicture) {
            /*
                Base64 encoding requires a byte array, the bitmap image cannot be converted directly into a byte array.
                so first convert the bitmap image into a ByteArrayOutputStream and then convert this stream into a byte array.
            */
	//	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	//	profilePicture.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
	//	byte[] array = byteArrayOutputStream.toByteArray();
	//	profile = Base64.encodeToString(array, Base64.DEFAULT);
	//}


	//private boolean validateProfile() {
	//	if (!IMAGE_STATUS)
	//		Toast.makeText(this, "Selecciona una foto de perfil", Toast.LENGTH_SHORT).show();
	//	return IMAGE_STATUS;
	//}

	private boolean validateNumber() {

		check = edtnumber.getText().toString();
		Log.e("inside number",check.length()+" ");
		if (check.length()>10) {
			return false;
		}else if(check.length()<10){
			return false;
		}
		return true;
	}

	private boolean validateCnfPass() {

		check = edtcnfpass.getText().toString();

		return check.equals(edtpass.getText().toString());
	}

	private boolean validatePass() {


		check = edtpass.getText().toString();

		if (check.length() < 4 || check.length() > 20) {
			return false;
		} else if (!check.matches("^[A-za-z0-9@]+")) {
			return false;
		}
		return true;
	}

	private boolean validateEmail() {

		check = edtemail.getText().toString();

		if (check.length() < 4 || check.length() > 40) {
			return false;
		} else if (!check.matches("^[A-za-z0-9.@]+")) {
			return false;
		} else if (!check.contains("@") || !check.contains(".")) {
			return false;
		}

		return true;
	}

	private boolean validateName() {

		check = edtname.getText().toString();

		return !(check.length() < 4 || check.length() > 20);

	}



	//TextWatcher for Name -----------------------------------------------------

	TextWatcher nameWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//none
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//none
		}

		@Override
		public void afterTextChanged(Editable s) {

			check = s.toString();

			if (check.length() < 4 || check.length() > 20) {
				edtname.setError("El nombre debe tener entre 4 y 20 caracteres");
			}
		}

	};

	//TextWatcher for Email -----------------------------------------------------

	TextWatcher emailWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//none
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//none
		}

		@Override
		public void afterTextChanged(Editable s) {

			check = s.toString();

			if (check.length() < 4 || check.length() > 40) {
				edtemail.setError("El correo debe tener entre 4 y 20 caracteres");
			} else if (!check.matches("^[A-za-z0-9.@]+")) {
				edtemail.setError("Es necesario el @ y el .");
			} else if (!check.contains("@") || !check.contains(".")) {
				edtemail.setError("Ingresa un Correo Válido");
			}

		}

	};

	//TextWatcher for pass -----------------------------------------------------

	TextWatcher passWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//none
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//none
		}

		@Override
		public void afterTextChanged(Editable s) {

			check = s.toString();

			if (check.length() < 4 || check.length() > 20) {
				edtpass.setError("La contraseña debe tener entre 4 y 20 caracteres");
			} else if (!check.matches("^[A-za-z0-9@]+")) {
				edtemail.setError("Solamente es permitido el @");
			}
		}

	};

	//TextWatcher for repeat Password -----------------------------------------------------

	TextWatcher cnfpassWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//none
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//none
		}

		@Override
		public void afterTextChanged(Editable s) {

			check = s.toString();

			if (!check.equals(edtpass.getText().toString())) {
				edtcnfpass.setError("Las contraseñas no coinciden");
			}
		}

	};


	//TextWatcher for Mobile -----------------------------------------------------

	TextWatcher numberWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//none
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//none
		}

		@Override
		public void afterTextChanged(Editable s) {

			check = s.toString();

			if (check.length()<10) {
				edtnumber.setError("Tu nombre de usuario debe ser mayor a 10 caracteres");
			}
		}

	};

}
