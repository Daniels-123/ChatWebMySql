package com.dybcatering.chatwebmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.AdaptadorMensajes;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.Mensaje;
import com.dybcatering.chatwebmysql.Conversacion.ConversacionLive4T;
import com.dybcatering.chatwebmysql.usersession.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ConversacionActivity extends AppCompatActivity implements AdaptadorMensajes.OnItemClickListener {

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

    private ImageView ButtonEmoji;
    private Button btnEnviarMensaje;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion);


        contentRoot = findViewById(R.id.contentRoot);
        edMessage = findViewById(R.id.textimput);
        ButtonEmoji = findViewById(R.id.emojiimage);
        btnEnviarMensaje = findViewById(R.id.btnEnviar);
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(edMessage.getText().toString());
            }
        });

        EmojIconActions emojIcon = new EmojIconActions(this, contentRoot, edMessage, ButtonEmoji);
        emojIcon.ShowEmojIcon();

        recyclergrupos = findViewById(R.id.messageRecyclerView);
        recyclergrupos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mItemMensajes = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(ConversacionActivity.this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        ObtenerDatos();


    }


    private void ObtenerDatos() {
        String url = "http://192.168.0.13/webdyb/loginapp/obtenerMensajes.php";
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


    public void enviarMensaje(final String s) {

        //String mensaje = etTexto.getText().toString();
        //   final String mensajeservidor = StringEscapeUtils.escapeJava(mensaje);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_SERVER),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
                       edMessage.setText("");
                       mItemMensajes.clear();
                       ObtenerDatos();

                        recyclergrupos.scrollToPosition(mItemMensajes.size()-1);
                        Toast.makeText(ConversacionActivity.this, response, Toast.LENGTH_LONG).show();
                        //					etTexto.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(ConversacionActivity.this, "ERROR EN LA CONEXIÓN"+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor

                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("usuario", "3");//usuario.getUsuario());
                parametros.put("grupo", "2");// usuarioDestino.getUsuario());

                parametros.put("mensaje", s);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {

    }
}
