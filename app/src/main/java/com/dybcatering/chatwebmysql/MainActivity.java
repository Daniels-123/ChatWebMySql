package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.AdaptadorGrupo;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.CrearGrupo.CrearGrupoActivity;
import com.dybcatering.chatwebmysql.usersession.UserSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdaptadorGrupo.OnItemClickListener {

	private HashMap<String, String> user;
	private String name, email, photo, mobile;
	private String first_time;
	RecyclerView recyclergrupos;
	UserSession sessionManager;
	private AdaptadorGrupo mAdaptadorGrupo;
	private ArrayList<Object> mItemGrupo;
	private RequestQueue mRequestQueue;

	FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sessionManager = new UserSession(this);
		sessionManager.checkLogin();
		fab = findViewById(R.id.btn_add_func);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CrearGrupoActivity.class);
				startActivity(intent);
			}
		});

		HashMap<String, String> user = sessionManager.getUserDetail();
		String nombreusuario = user.get(UserSession.NAME);

		recyclergrupos = findViewById(R.id.rvgrupos);
		recyclergrupos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

		mItemGrupo = new ArrayList<>();

		mRequestQueue = Volley.newRequestQueue(MainActivity.this);

		ObtenerDatos();

		Toast.makeText(this, "El texto es "+ nombreusuario, Toast.LENGTH_SHORT).show();
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


	private void ObtenerDatos() {

		String url = "http://192.168.1.101/loginapp/listargrupos.php";
		final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setMessage("Cargando...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject jsonObject = new JSONObject(response);
							JSONArray jsonArray = jsonObject.getJSONArray("Grupos");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject hit = jsonArray.getJSONObject(i);
								String name = hit.getString("name");


								mItemGrupo.add(new ItemGrupo(name));
							}
							mAdaptadorGrupo = new AdaptadorGrupo(MainActivity.this,mItemGrupo);
							recyclergrupos.setAdapter(mAdaptadorGrupo);
							mAdaptadorGrupo.setOnClickItemListener(MainActivity.this);

						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
							Toast toast= Toast.makeText(MainActivity.this,
									"Parece que algo salió mal o aun no has agregado cursos", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_HORIZONTAL, 0, 0);
							toast.show();
							//Toasty toasty = Toasty.error(getContext(), "Parece que algo salió mal o aun no has agregado cursos", Toast.LENGTH_SHORT).show();

						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				progressDialog.dismiss();
				Toast.makeText(MainActivity.this, "error de bd", Toast.LENGTH_SHORT).show();
			}
		}) ;
		/* {
			@Override
			protected Map<String, String > getParams(){
				Map<String, String> params = new HashMap<>();
				params.put("id", id);
				return params;
			}
		};*/

		RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
		requestQueue.add(stringRequest);
	}

	@Override
	public void onItemClick(int position) {

	}
}
