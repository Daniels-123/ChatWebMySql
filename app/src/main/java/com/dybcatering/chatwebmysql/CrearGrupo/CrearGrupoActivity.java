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


		//toolbar = findViewById(R.id.toolbar);


//		EmojiCompat.Config config = new BundledEmojiCompatConfig(this)
//				.setEmojiSpanIndicatorEnabled(true)
//				.setReplaceAll(true);
//		EmojiCompat.init(config);
		setContentView(R.layout.activity_crear_grupo);
//		edtemoji = findViewById(R.id.nombregrupo);
//		edtbtn = findViewById(R.id.emobtn);
//		edttextview = findViewById(R.id.emojiresultado);

//		edtemoji.setText(new StringBuilder(new String(Character.toChars(0x2764))).append("Mostrar Datos"));

/*		edtbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edttextview.setText(edtemoji.getText().toString());
				Toast.makeText(CrearGrupoActivity.this, "el texto es "+ edtemoji.getText().toString(), Toast.LENGTH_SHORT).show();
			}
		});*/

		btnSelection = (Button) findViewById(R.id.btn_show);

	//	studentList = new ArrayList<Student>();


		/***
		 *  Se eliminan los metodos de ejemplo
		 */
	/*	for (int i = 1; i <= 15; i++) {
			Student st = new Student("Student " + i, "androidstudent" + i
					+ "@gmail.com", false);

			studentList.add(st);
		}*/


		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		// create an Object for Adapter
		/*mAdapter = new CardViewDataAdapter(studentList);

		// set the adapter object to the Recyclerview
		mRecyclerView.setAdapter(mAdapter);

		btnSelection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String data = "";
				List<Student> stList = ((CardViewDataAdapter) mAdapter)
						.getStudentist();

				for (int i = 0; i < stList.size(); i++) {
					Student singleStudent = stList.get(i);
					if (singleStudent.isSelected() == true) {

						data = data + "\n" + singleStudent.getName().toString();

					}

				}

				Toast.makeText(CrearGrupoActivity.this,
						"Estudiantes Seleccionados: \n" + data, Toast.LENGTH_LONG)
						.show();
			}
		});
*/

		mItemUsuario = new ArrayList<>();

		mRequestQueue = Volley.newRequestQueue(CrearGrupoActivity.this);


		ObtenerDatos();

	}

	private void ObtenerDatos() {

		String url = "http://192.168.43.188/webdyb/loginapp/listarusuarios.php";
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
