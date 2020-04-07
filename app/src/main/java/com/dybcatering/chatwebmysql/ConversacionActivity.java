package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.AdaptadorGrupo;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.AdaptadorMensajes;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.Mensaje;
import com.dybcatering.chatwebmysql.usersession.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConversacionActivity extends AppCompatActivity implements AdaptadorMensajes.OnItemClickListener {

	RecyclerView recyclergrupos;
	UserSession sessionManager;
	private AdaptadorMensajes mAdaptadorMensajes;
	private ArrayList<Object> mItemMensajes;
	private RequestQueue mRequestQueue;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversacion);

		recyclergrupos = findViewById(R.id.rvMensajes);
		recyclergrupos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

		mItemMensajes = new ArrayList<>();

		mRequestQueue = Volley.newRequestQueue(ConversacionActivity.this);

		ObtenerDatos();
	}

	private void ObtenerDatos() {
		String url = "http://192.168.1.101/loginapp/obtenerMensajes.php";
		final ProgressDialog progressDialog = new ProgressDialog(ConversacionActivity.this);
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
							JSONArray jsonArray = jsonObject.getJSONArray("Mensajes");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject hit = jsonArray.getJSONObject(i);
								String mensaje = hit.getString("MENSAJE");
								String nombreusuario = hit.getString("NOMBRE_USUARIO");
								String nombregrupo = hit.getString("NOMBRE_GRUPO");
								String enviado = hit.getString("ENVIADO");


								mItemMensajes.add(new Mensaje(mensaje, nombreusuario, nombregrupo, enviado));
							}
							mAdaptadorMensajes = new AdaptadorMensajes(ConversacionActivity.this,mItemMensajes);
							recyclergrupos.setAdapter(mAdaptadorMensajes);
							mAdaptadorMensajes.setOnClickItemListener(ConversacionActivity.this);

						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
							Toast toast= Toast.makeText(ConversacionActivity.this,
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
				Toast.makeText(ConversacionActivity.this, "error de bd", Toast.LENGTH_SHORT).show();
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

		RequestQueue requestQueue = Volley.newRequestQueue(ConversacionActivity.this);
		requestQueue.add(stringRequest);
	}




	@Override
	public void onItemClick(int position) {

	}
}
