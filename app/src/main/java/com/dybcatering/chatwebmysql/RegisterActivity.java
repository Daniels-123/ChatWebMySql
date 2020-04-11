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
//import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.dybcatering.chatwebmysql.request.RegisterRequest;
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

									//sendRegistrationEmail(name,email);


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
	/*private void sendRegistrationEmail(final String name, final String emails) {


		BackgroundMail.newBuilder(RegisterActivity.this)
				.withSendingMessage("Envío de saludos de bienvenida a su correo electrónico !")
				.withSendingMessageSuccess("Compruebe amablemente su correo electrónico ahora!")
				.withSendingMessageError("¡Error al enviar la contraseña! Inténtalo de nuevo !")
				.withUsername("prueba.correo7521@gmail.com")
				.withPassword("N1m2g3e4r57791")
				.withMailto(emails)
				.withType(BackgroundMail.TYPE_HTML)
				.withSubject("Saludos desde la aplicación")
				.withBody("\t<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"x-apple-disable-message-reformatting\" /><meta name=\"apple-mobile-web-app-capable\" content=\"yes\" /><meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" /><meta name=\"format-detection\" content=\"telephone=no\" /><title></title><style type=\"text/css\">\n" +
						" Resets \n" +
						"\t\t\t.ReadMsgBody { width: 100%; background-color: #ebebeb;}\n" +
						"\t\t\t.ExternalClass {width: 100%; background-color: #ebebeb;}\n" +
						"\t\t\t.ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {line-height:100%;}\n" +
						"\t\t\ta[x-apple-data-detectors]{\n" +
						"\t\t\t\tcolor:inherit !important;\n" +
						"\t\t\t\ttext-decoration:none !important;\n" +
						"\t\t\t\tfont-size:inherit !important;\n" +
						"\t\t\t\tfont-family:inherit !important;\n" +
						"\t\t\t\tfont-weight:inherit !important;\n" +
						"\t\t\t\tline-height:inherit !important;\n" +
						"\t\t\t}        \n" +
						"\t\t\tbody {-webkit-text-size-adjust:none; -ms-text-size-adjust:none;}\n" +
						"\t\t\tbody {margin:0; padding:0;}\n" +
						"\t\t\t.yshortcuts a {border-bottom: none !important;}\n" +
						"\t\t\t.rnb-del-min-width{ min-width: 0 !important; }\n" +
						"\n" +
						"\t\t\t Add new outlook css start \n" +
						"\t\t\t.templateContainer{\n" +
						"\t\t\t\tmax-width:590px !important;\n" +
						"\t\t\t\twidth:auto !important;\n" +
						"\t\t\t}\n" +
						"\t\t\t/* Add new outlook css end \n" +
						"\n" +
						"\t\t\t/* Image width by default for 3 columns \n" +
						"\t\t\timg[class=\"rnb-col-3-img\"] {\n" +
						"\t\t\tmax-width:170px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Image width by default for 2 columns \n" +
						"\t\t\timg[class=\"rnb-col-2-img\"] {\n" +
						"\t\t\tmax-width:264px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Image width by default for 2 columns aside small size \n" +
						"\t\t\timg[class=\"rnb-col-2-img-side-xs\"] {\n" +
						"\t\t\tmax-width:180px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Image width by default for 2 columns aside big size \n" +
						"\t\t\timg[class=\"rnb-col-2-img-side-xl\"] {\n" +
						"\t\t\tmax-width:350px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Image width by default for 1 column \n" +
						"\t\t\timg[class=\"rnb-col-1-img\"] {\n" +
						"\t\t\tmax-width:550px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Image width by default for header \n" +
						"\t\t\timg[class=\"rnb-header-img\"] {\n" +
						"\t\t\tmax-width:590px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* Ckeditor line-height spacing \n" +
						"\t\t\t.rnb-force-col p, ul, ol{margin:0px!important;}\n" +
						"\t\t\t.rnb-del-min-width p, ul, ol{margin:0px!important;}\n" +
						"\n" +
						"\t\t\t/* tmpl-2 preview \n" +
						"\t\t\t.rnb-tmpl-width{ width:100%!important;}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.rnb-social-width{padding-right:15px!important;}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.rnb-social-align{float:right!important;}\n" +
						"\n" +
						"\t\t\t/* Ul Li outlook extra spacing fix \n" +
						"\t\t\tli{mso-margin-top-alt: 0; mso-margin-bottom-alt: 0;}        \n" +
						"\n" +
						"\t\t\t/* Outlook fix \n" +
						"\t\t\ttable {mso-table-lspace:0pt; mso-table-rspace:0pt;}\n" +
						"\t\t\n" +
						"\t\t\t/* Outlook fix \n" +
						"\t\t\ttable, tr, td {border-collapse: collapse;}\n" +
						"\n" +
						"\t\t\t/* Outlook fix \n" +
						"\t\t\tp,a,li,blockquote {mso-line-height-rule:exactly;} \n" +
						"\n" +
						"\t\t\t/* Outlook fix \n" +
						"\t\t\t.msib-right-img { mso-padding-alt: 0 !important;}\n" +
						"\n" +
						"\t\t\t/* Fix text line height on preview  \n" +
						"\t\t\t.content-spacing div {display: list-item; list-style-type: none;}\n" +
						"\n" +
						"\t\t\t@media only screen and (min-width:590px){\n" +
						"\t\t\t/* mac fix width \n" +
						"\t\t\t.templateContainer{width:590px !important;}\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t@media screen and (max-width: 360px){\n" +
						"\t\t\t/* yahoo app fix width \"tmpl-2 tmpl-10 tmpl-13\" in android devices \n" +
						"\t\t\t.rnb-yahoo-width{ width:360px !important;}\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t@media screen and (max-width: 380px){\n" +
						"\t\t\t/* fix width and font size \"tmpl-4 tmpl-6\" in mobile preview \n" +
						"\t\t\t.element-img-text{ font-size:24px !important;}\n" +
						"\t\t\t.element-img-text2{ width:230px !important;}\n" +
						"\t\t\t.content-img-text-tmpl-6{ font-size:24px !important;}\n" +
						"\t\t\t.content-img-text2-tmpl-6{ width:220px !important;}\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t@media screen and (max-width: 480px) {\n" +
						"\t\t\ttd[class=\"rnb-container-padding\"] {\n" +
						"\t\t\tpadding-left: 10px !important;\n" +
						"\t\t\tpadding-right: 10px !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* force container nav to (horizontal) blocks \n" +
						"\t\t\ttd.rnb-force-nav {\n" +
						"\t\t\tdisplay: inherit;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* fix text alignment \"tmpl-11\" in mobile preview \n" +
						"\t\t\t.rnb-social-text-left {\n" +
						"\t\t\twidth: 100%;\n" +
						"\t\t\ttext-align: center;\n" +
						"\t\t\tmargin-bottom: 15px;\n" +
						"\t\t\t}\n" +
						"\t\t\t.rnb-social-text-right {\n" +
						"\t\t\twidth: 100%;\n" +
						"\t\t\ttext-align: center;\n" +
						"\t\t\t}\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t@media only screen and (max-width: 600px) {\n" +
						"\n" +
						"\t\t\t/* center the address &amp; social icons \n" +
						"\t\t\t.rnb-text-center {text-align:center !important;}\n" +
						"\n" +
						"\t\t\t/* force container columns to (horizontal) blocks \n" +
						"\t\t\ttd.rnb-force-col {\n" +
						"\t\t\tdisplay: block;\n" +
						"\t\t\tpadding-right: 0 !important;\n" +
						"\t\t\tpadding-left: 0 !important;\n" +
						"\t\t\twidth:100%;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-container {\n" +
						"\t\t\t width: 100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-btn-col-content {\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\t\t\t}\n" +
						"\t\t\ttable.rnb-col-3 {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\n" +
						"\t\t\t/* change left/right padding and margins to top/bottom ones \n" +
						"\t\t\tmargin-bottom: 10px;\n" +
						"\t\t\tpadding-bottom: 10px;\n" +
						"\t\t\t/*border-bottom: 1px solid #eee;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-last-col-3 {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-col-2 {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\n" +
						"\t\t\t/* change left/right padding and margins to top/bottom ones \n" +
						"\t\t\tmargin-bottom: 10px;\n" +
						"\t\t\tpadding-bottom: 10px;\n" +
						"\t\t\t/*border-bottom: 1px solid #eee;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-col-2-noborder-onright {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\n" +
						"\t\t\t/* change left/right padding and margins to top/bottom ones \n" +
						"\t\t\tmargin-bottom: 10px;\n" +
						"\t\t\tpadding-bottom: 10px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-col-2-noborder-onleft {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\n" +
						"\t\t\t/* change left/right padding and margins to top/bottom ones \n" +
						"\t\t\tmargin-top: 10px;\n" +
						"\t\t\tpadding-top: 10px;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-last-col-2 {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttable.rnb-col-1 {\n" +
						"\t\t\t/* unset table align=\"left/right\" \n" +
						"\t\t\tfloat: none !important;\n" +
						"\t\t\twidth: 100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-col-3-img {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-col-2-img {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-col-2-img-side-xs {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-col-2-img-side-xl {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-col-1-img {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-header-img {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\tmargin:0 auto;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\timg.rnb-logo-img {\n" +
						"\t\t\t/**max-width:none !important;*\n" +
						"\t\t\twidth:100% !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\ttd.rnb-mbl-float-none {\n" +
						"\t\t\tfloat:inherit !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t.img-block-center{text-align:center !important;}\n" +
						"\n" +
						"\t\t\t.logo-img-center\n" +
						"\t\t\t{\n" +
						"\t\t\t\tfloat:inherit !important;\n" +
						"\t\t\t}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.rnb-social-align{margin:0 auto !important; float:inherit !important;}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.rnb-social-center{display:inline-block;}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.social-text-spacing{margin-bottom:0px !important; padding-bottom:0px !important;}\n" +
						"\n" +
						"\t\t\t/* tmpl-11 preview \n" +
						"\t\t\t.social-text-spacing2{padding-top:15px !important;}\n" +
						"\n" +
						"\t\t}</style><!--[if gte mso 11]><style type=\"text/css\">table{border-spacing: 0; }table td {border-collapse: separate;}</style><![endif]--><!--[if !mso]><!--><style type=\"text/css\">table{border-spacing: 0;} table td {border-collapse: collapse;}</style> <!--<![endif]--><!--[if gte mso 15]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]--><!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]--></head><body>\n" +
						"\n" +
						"\t<table border=\"0\" align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"main-template\" bgcolor=\"#ffffff\" style=\"background-color: rgb(255, 255, 255);\">\n" +
						"\n" +
						"\t\t<tbody><tr style=\"display:none !important; font-size:1px; mso-hide: all;\"><td></td><td></td></tr><tr>\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\t\t\t<!--[if gte mso 9]>\n" +
						"\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"590\" style=\"width:590px;\">\n" +
						"\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t<td align=\"center\" valign=\"top\" width=\"590\" style=\"width:590px;\">\n" +
						"\t\t\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\" style=\"max-width:590px!important; width: 590px;\">\n" +
						"\t\t\t<tbody><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<table class=\"rnb-del-min-width\" width=\"100%\" cellpadding=\"0\" border=\"0\" cellspacing=\"0\" style=\"min-width:590px;\" name=\"Layout_2620\" id=\"Layout_2620\">\n" +
						"\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t<td class=\"rnb-del-min-width\" valign=\"top\" align=\"center\" style=\"min-width:590px;\">\n" +
						"\t\t\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" border=\"0\" height=\"30\" cellspacing=\"0\">\n" +
						"\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t<td valign=\"top\" height=\"30\">\n" +
						"\t\t\t\t\t\t\t\t\t\t<img width=\"20\" height=\"30\" style=\"display:block; max-height:30px; max-width:20px;\" alt=\"\" src=\"http://img.mailinblue.com/new_images/rnb/rnb_space.gif\">\n" +
						"\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t</td>\n" +
						"\t\t</tr><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<div style=\"background-color: rgb(255, 255, 255); border-radius: 0px;\">\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
						"\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<td valign=\"top\" width=\"590\" style=\"width:590px;\">\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t<table class=\"rnb-del-min-width\" width=\"100%\" cellpadding=\"0\" border=\"0\" cellspacing=\"0\" style=\"min-width:590px;\" name=\"Layout_1\" id=\"Layout_1\">\n" +
						"\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t<td class=\"rnb-del-min-width\" align=\"center\" valign=\"top\" style=\"min-width:590px;\">\n" +
						"\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"rnb-container\" bgcolor=\"#ffffff\" style=\"background-color: rgb(255, 255, 255); border-bottom: 1px solid rgb(200, 200, 200); border-radius: 0px; padding-left: 20px; padding-right: 20px; border-collapse: separate;\">\n" +
						"\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t<td height=\"40\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t<td valign=\"top\" class=\"rnb-container-padding\" align=\"left\">\n" +
						"\t\t\t\t\t\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" border=\"0\" align=\"center\" cellspacing=\"0\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" align=\"center\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<table cellpadding=\"0\" border=\"0\" align=\"left\" cellspacing=\"0\" class=\"logo-img-center\"> \n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"middle\" align=\"center\" style=\"line-height: 1px;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:0px None #000;border-right:0px None #000;border-bottom:0px None #000;border-left:0px None #000;display:inline-block; \" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><div><img width=\"200\" vspace=\"0\" hspace=\"0\" border=\"0\" alt=\"Sendinblue\" style=\"float: left;max-width:200px;display:block;\" class=\"rnb-logo-img\" src=\"https://imageneslive4teach.000webhostapp.com/imagenes/liveteach_logo.png\"></div></div></td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t</tbody></table></td>\n" +
						"\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t<td height=\"40\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\n" +
						"\t\t\t</div></td>\n" +
						"\t\t</tr><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<div style=\"background-color: rgb(255, 255, 255); border-radius: 0px;\">\n" +
						"\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
						"\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<td valign=\"top\" width=\"590\" style=\"width:590px;\">\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t<table class=\"rnb-del-min-width\" width=\"100%\" cellpadding=\"0\" border=\"0\" cellspacing=\"0\" style=\"min-width:100%;\" name=\"Layout_5\">\n" +
						"\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t<td class=\"rnb-del-min-width\" align=\"center\" valign=\"top\">\n" +
						"\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"rnb-container\" bgcolor=\"#ffffff\" style=\"background-color: rgb(255, 255, 255); padding-left: 20px; padding-right: 20px; border-collapse: separate; border-radius: 0px; border-bottom: 2px solid rgb(200, 200, 200);\">\n" +
						"\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"30\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" class=\"rnb-container-padding\" align=\"left\">\n" +
						"\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"rnb-columns-container\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"rnb-force-col\" valign=\"top\" style=\"padding-right: 0px;\">\n" +
						"\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" valign=\"top\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"left\" class=\"rnb-col-1\">\n" +
						"\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"content-spacing\" style=\"font-size:16px; font-family:'Arial',Helvetica,sans-serif, sans-serif; color:#999; line-height: 21px;\"><div>\n" +
						"\t<div style=\"line-height:150%;\"><span style=\"font-size:18px;\"><span style=\"color:#33c0c9;\"><strong>Hola Sr/Srta"+ name +" ,</strong></span></span></div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\">&nbsp;</div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\">¡Este es Diago y Benitez, CEO de Web API! ¡Bienvenido a bordo con nosotros en nombre de nuestro equipo!,<a href=\"#\" style=\"text-decoration: solid; color: rgb(51, 192, 201);\" target=\"_blank\"></a>. ¡Prometemos brindarle una experiencia de compra inolvidable en el futuro!.</div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\">&nbsp;</div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\"><strong>Entonces, ¿qué estás esperando, comienza a comprar y obtén lo mejor de nuestros productos justo en tu puerta!           ¡Estamos felices de ayudarte! ¡En caso de cualquier consulta, contáctenos a nuestros manejadores mencionados a continuación!</strong></div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\">&nbsp;</div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\"><span style=\"color:#33c0c9;\"><span style=\"font-size:18px;\"><strong>Saludos cordiales,</strong></span></span></div>\n" +
						"\n" +
						"\t<div style=\"line-height:150%;\">&nbsp;</div>\n" +
						"\n" +
						"\t<div style=\"line-height:100%;\">Live4Teach<br>\n" +
						"\t&nbsp;</div>\n" +
						"\n" +
						"\t<div style=\"line-height:100%;\"><span style=\"font-size:12px;\"><em>President/CEO<br>\n" +
						"\tAirBound</em></span></div>\n" +
						"\t</div>\n" +
						"\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody></table>\n" +
						"\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td></tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody></table></td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"30\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t</tbody></table><!--[if mso]>\n" +
						"\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\n" +
						"\t\t\t\t</div></td>\n" +
						"\t\t</tr><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<div style=\"background-color: rgb(255, 255, 255); border-radius: 0px;\">\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
						"\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t<td valign=\"top\" width=\"590\" style=\"width:590px;\">\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<!--[if mso]>\n" +
						"\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t</div></td>\n" +
						"\t\t</tr><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<div style=\"background-color: rgb(255, 255, 255);\">\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<table class=\"rnb-del-min-width rnb-tmpl-width\" width=\"100%\" cellpadding=\"0\" border=\"0\" cellspacing=\"0\" style=\"min-width:590px;\" name=\"Layout_3\" id=\"Layout_3\">\n" +
						"\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t<td class=\"rnb-del-min-width\" align=\"center\" valign=\"top\" bgcolor=\"#ffffff\" style=\"min-width:590px; background-color: #ffffff; text-align: center;\">\n" +
						"\t\t\t\t\t\t\t\t<table width=\"590\" class=\"rnb-container\" cellpadding=\"0\" border=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#ffffff\" style=\"padding-right: 20px; padding-left: 20px; background-color: rgb(255, 255, 255);\">\n" +
						"\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td height=\"10\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-size:14px; color:#919191; font-weight:normal; text-align:center; font-family:'Arial',Helvetica,sans-serif;\"><div>\n" +
						"\t<div style=\"line-height:200%;\">Este correo fue enviado a "+email+"</div>\n" +
						"\n" +
						"\t<div style=\"line-height:200%;\">You received this email because you are registered with Your Company</div>\n" +
						"\n" +
						"\t<div>&nbsp;</div>\n" +
						"\t</div>\n" +
						"\t</div>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<div style=\"display: block; text-align: center;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"font-size:14px; font-weight:normal; display: inline-block; text-align:center; font-family:'Arial',Helvetica,sans-serif;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<a style=\"text-decoration:none; color:#ccc;font-size:14px;font-weight:normal;font-family:'Arial',Helvetica,sans-serif;\" target=\"_blank\" href=\"{{ unsubscribe }}\"></a></span>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
						"\t\t\t\t\t\t\t\t\t\t</td></tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td height=\"10\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\n" +
						"\t\t\t\t\t\t\t\t\t</tr><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td height=\"10\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr></tbody></table>\n" +
						"\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t</div></td>\n" +
						"\t\t</tr><tr>\n" +
						"\n" +
						"\t\t\t<td align=\"center\" valign=\"top\">\n" +
						"\n" +
						"\t\t\t\t<div style=\"background-color: rgb(51, 192, 201);\">\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t\t<table class=\"rnb-del-min-width rnb-tmpl-width\" width=\"100%\" cellpadding=\"0\" border=\"0\" cellspacing=\"0\" style=\"min-width:590px;\" name=\"Layout_4\" id=\"Layout_4\">\n" +
						"\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t<td class=\"rnb-del-min-width\" align=\"center\" valign=\"top\" style=\"min-width:590px;\">\n" +
						"\t\t\t\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" border=\"0\" align=\"center\" cellspacing=\"0\" bgcolor=\"#33c0c9\" style=\"padding-right: 20px; padding-left: 20px; background-color: rgb(51, 192, 201);\">\n" +
						"\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td height=\"20\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td style=\"font-size:14px; color:#ffffff; font-weight:normal; text-align:center; font-family:'Arial',Helvetica,sans-serif;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<div><div>© 2020 Live4Teach</div>\n" +
						"\t</div>\n" +
						"\t\t\t\t\t\t\t\t\t\t</td></tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td height=\"20\" style=\"font-size:1px; line-height:0px; mso-hide: all;\">&nbsp;</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</tbody></table>\n" +
						"\t\t\t\t\t\n" +
						"\t\t\t\t</div></td>\n" +
						"\t\t</tr></tbody></table>\n" +
						"\t\t\t\t<!--[if gte mso 9]>\n" +
						"\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t\t\t<![endif]-->\n" +
						"\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t</tr>\n" +
						"\t\t\t</tbody></table>\n" +
						"\n" +
						"\t</body></html>")
						//"<p>Hola Mr/Miss, <b>'"+ name + "'</b></p>"+ "\n " + getString(R.string.registermail1))
				.send();



	}*/
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
