package com.dybcatering.chatwebmysql.CrearGrupo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.emoji.bundled.BundledEmojiCompatConfig;
//import androidx.emoji.text.EmojiCompat;
//import androidx.emoji.widget.EmojiButton;
//import androidx.emoji.widget.EmojiEditText;
//import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.AdaptadorCrearUsuarioGrupo.AdaptadorEstudiante;
import com.dybcatering.chatwebmysql.AdaptadorCrearUsuarioGrupo.ItemUsuario;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.AdaptadorGrupo;
import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.AdaptadorCreacionUsuarios.CardViewDataAdapter;
import com.dybcatering.chatwebmysql.AdaptadorCreacionUsuarios.Student;
import com.dybcatering.chatwebmysql.MainActivity;
import com.dybcatering.chatwebmysql.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CrearGrupoActivity extends AppCompatActivity {

	private Toolbar toolbar;

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	//private List<Student> studentList;

	private Button btnSelection;

//	EmojiTextView edttextview;
//	EmojiButton edtbtn;
//	EmojiEditText edtemoji;

	private AdaptadorEstudiante mAdaptadorUsuario;
	private ArrayList<Object> mItemUsuario;
	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_crear_grupo);

		btnSelection = (Button) findViewById(R.id.btn_show);



		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		mRecyclerView.setHasFixedSize(true);


		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


		mItemUsuario = new ArrayList<>();

		mRequestQueue = Volley.newRequestQueue(CrearGrupoActivity.this);


		ObtenerDatos();

	}

	private void ObtenerDatos() {

		String url = "http://192.168.0.13/webdyb/loginapp/listarusuarios.php";
		final ProgressDialog progressDialog = new ProgressDialog(CrearGrupoActivity.this);
		progressDialog.setMessage("Cargando...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject jsonObject = new JSONObject(response);
							JSONArray jsonArray = jsonObject.getJSONArray("Usuarios");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject hit = jsonArray.getJSONObject(i);
								String name = hit.getString("name");


								mItemUsuario.add(new ItemUsuario(name));
							}
							mAdaptadorUsuario = new AdaptadorEstudiante(CrearGrupoActivity.this,mItemUsuario);
							mRecyclerView.setAdapter(mAdaptadorUsuario);
//							mAdaptadorUsuario.setOnClickItemListener(CrearGrupoActivity.this);

						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
							Toast toast= Toast.makeText(CrearGrupoActivity.this,
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
				Toast.makeText(CrearGrupoActivity.this, "error de bd", Toast.LENGTH_SHORT).show();
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

		RequestQueue requestQueue = Volley.newRequestQueue(CrearGrupoActivity.this);
		requestQueue.add(stringRequest);
	}
	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}
}
