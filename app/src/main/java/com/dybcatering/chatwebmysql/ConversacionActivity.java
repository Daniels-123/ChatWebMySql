package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.emoji.bundled.BundledEmojiCompatConfig;
//import androidx.emoji.text.EmojiCompat;
//import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

//import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConversacionActivity extends AppCompatActivity  implements EmojiconGridFragment.OnEmojiconClickedListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener, AdaptadorMensajes.OnItemClickListener {

	RecyclerView recyclergrupos;
	UserSession sessionManager;
	private AdaptadorMensajes mAdaptadorMensajes;
	private ArrayList<Object> mItemMensajes;
	private RequestQueue mRequestQueue;
	final Handler handler = new Handler();
	Timer timer = new Timer();
	//EmojiEditText etTexto;
	Button btEnviar;
	LinearLayout linearhorizontal;
	ScrollView scrollView;
	EmojiconEditText mEditText;
	EmojiconTextView mTextView;
	Button button;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	EmojiCompat.Config config = new BundledEmojiCompatConfig(this)
	//			.setReplaceAll(true);
	//	EmojiCompat.init(config);
		setContentView(R.layout.activity_conversacion);

		mEditText = findViewById(R.id.editEmojicon);
		button = findViewById(R.id.abriremojis);
		linearhorizontal = findViewById(R.id.linearhorizontal);
		scrollView = findViewById(R.id.scroll);

	/*	mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mTextView.setText(s);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});*/


		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linearhorizontal.setVisibility(View.GONE);
				setEmojiconFragment(false);

			}
		});



	//	etTexto = findViewById(R.id.etTexto);
		recyclergrupos = findViewById(R.id.rvMensajes);
        recyclergrupos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
		mItemMensajes = new ArrayList<>();

		mRequestQueue = Volley.newRequestQueue(ConversacionActivity.this);

		//btEnviar = findViewById(R.id.btnEnviar);
	/*	TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							//Ejecuta tu AsyncTask!
							AsyncTask myTask = new AsyncTask() {
								@Override
								protected Object doInBackground(Object[] objects) {
									return null;
								}
							};
							ObtenerDatos();
							myTask.execute();
						} catch (Exception e) {
							Log.e("error", e.getMessage());
						}
					}
				});
			}
		};

		timer.schedule(task, 0, 3000);*/
		ObtenerDatos();

//		btEnviar.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
	//			if(etTexto.getText().toString().isEmpty()) {
//					Toast.makeText(ConversacionActivity.this, "Se te olvido escribir el mensaje.", Toast.LENGTH_LONG).show();
	//			} else {
//					enviarMensaje();
				//	ObtenerDatos();
	//			}
//			}
//		});

//		btEnviar.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				Toast.makeText(ConversacionActivity.this, "el texto es hola", Toast.LENGTH_SHORT).show();
//				GrabarVoz();
//
//				return true;
//			}
//		});

	}



	private void GrabarVoz() {
	}


	private void ObtenerDatos() {
		String url = "http://192.168.0.11/webdyb/loginapp/obtenerMensajes.php";
//		final ProgressDialog progressDialog = new ProgressDialog(ConversacionActivity.this);
	//	progressDialog.setMessage("Cargando...");
	//	progressDialog.show();
//		progressDialog.setCancelable(false);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//progressDialog.dismiss();
						try {
							JSONObject jsonObject = new JSONObject(response);
							JSONArray jsonArray = jsonObject.getJSONArray("Mensajes");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject hit = jsonArray.getJSONObject(i);
								String mensaje = hit.getString("MENSAJE");
								String nombreusuario = hit.getString("NOMBRE_USUARIO");
								String nombregrupo = hit.getString("NOMBRE_GRUPO");
								String enviado = hit.getString("ENVIADO");
								String tipo = hit.getString("TIPO_MENSAJE");


								mItemMensajes.add(new Mensaje(mensaje, nombreusuario, nombregrupo, enviado, tipo));
							}
							mAdaptadorMensajes = new AdaptadorMensajes(ConversacionActivity.this,mItemMensajes);
							recyclergrupos.setAdapter(mAdaptadorMensajes);

							mAdaptadorMensajes.setOnClickItemListener(ConversacionActivity.this);

						} catch (JSONException e) {
							e.printStackTrace();
						//	progressDialog.dismiss();
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
			//	progressDialog.dismiss();
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


	public void enviarMensaje() {

	    //String mensaje = etTexto.getText().toString();
        //   final String mensajeservidor = StringEscapeUtils.escapeJava(mensaje);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_SERVER),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// En este apartado se programa lo que deseamos hacer en caso de no haber errores
						Toast.makeText(ConversacionActivity.this, response, Toast.LENGTH_LONG).show();
						ObtenerDatos();
	//					etTexto.setText("");
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// En caso de tener algun error en la obtencion de los datos
				Toast.makeText(ConversacionActivity.this, "ERROR EN LA CONEXIÓN", Toast.LENGTH_LONG).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				// En este metodo se hace el envio de valores de la aplicacion al servidor

				Map<String, String> parametros = new Hashtable<String, String>();
				parametros.put("usuario", "3");//usuario.getUsuario());
				parametros.put("grupo", "2");// usuarioDestino.getUsuario());

      //          parametros.put("mensaje", etTexto.getText().toString());
                return parametros;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}


	private void setEmojiconFragment(boolean useSystemDefault) {


		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
				.commit();

	}


	@Override
	public void onItemClick(int position) {

	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		EmojiconsFragment.input(mEditText, emojicon);
	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		EmojiconsFragment.backspace(mEditText);
	}
}
