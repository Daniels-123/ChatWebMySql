package com.dybcatering.chatwebmysql.Conversacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.AdaptadorMensajes;
import com.dybcatering.chatwebmysql.AdaptadorMensajes.Mensaje;
import com.dybcatering.chatwebmysql.BuildConfig;
import com.dybcatering.chatwebmysql.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ConversacionLive4T extends AppCompatActivity implements AdaptadorMensajes.OnItemClickListener, View.OnClickListener { //} View.OnClickListener, ClickListenerChatFirebase  {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    static final String TAG = ConversacionLive4T.class.getSimpleName();
    static final String CHAT_REFERENCE = "chatmodel";

    //Firebase and GoogleApiClient
    //private FirebaseAuth mFirebaseAuth;
    //private FirebaseUser mFirebaseUser;
    //private GoogleApiClient mGoogleApiClient;
    //private DatabaseReference mFirebaseDatabaseReference;
    // FirebaseStorage storage = FirebaseStorage.getInstance();

    //CLass Model
    //private UserModel userModel;

    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;

    private EmojIconActions emojIcon;

    //File
    private File filePathImageCamera;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ImageView ButtonEmoji;
    private Button btnEnviarMensaje;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private AdaptadorMensajes mAdaptadorMensajes;
    private ArrayList<Object> mItemMensajes;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);


        contentRoot = findViewById(R.id.contentRoot);
        edMessage = findViewById(R.id.textimput);
        ButtonEmoji = findViewById(R.id.emojiimage);
        btnEnviarMensaje = findViewById(R.id.btnEnviar);
        btnEnviarMensaje.setOnClickListener(ConversacionLive4T.this);

        EmojIconActions emojIcon = new EmojIconActions(this, contentRoot, edMessage, ButtonEmoji);
        emojIcon.ShowEmojIcon();

        rvListMessage = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mItemMensajes = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(ConversacionLive4T.this);

        lerMessagensFirebase();

    }

    private void lerMessagensFirebase() {
        String url = "http://192.168.0.13/webdyb/loginapp/obtenerMensajes.php";
//		final ProgressDialog progressDialog = new ProgressDialog(ConversacionLive4T.this);
        //	progressDialog.setMessage("Cargando...");
        //	progressDialog.show();
//		progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
                            mAdaptadorMensajes = new AdaptadorMensajes(ConversacionLive4T.this, mItemMensajes);
                            rvListMessage.setAdapter(mAdaptadorMensajes);

                            mAdaptadorMensajes.setOnClickItemListener(ConversacionLive4T.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //	progressDialog.dismiss();
                            Toast toast = Toast.makeText(ConversacionLive4T.this,
                                    "Parece que algo salió mal o aun no has agregado cursos", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            //Toasty toasty = Toasty.error(getContext(), "Parece que algo salió mal o aun no has agregado cursos", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //	progressDialog.dismiss();
                Toast.makeText(ConversacionLive4T.this, "error de bd", Toast.LENGTH_SHORT).show();
            }
        });
		/* {
			@Override
			protected Map<String, String > getParams(){
				Map<String, String> params = new HashMap<>();
				params.put("id", id);
				return params;
			}
		};*/

        RequestQueue requestQueue = Volley.newRequestQueue(ConversacionLive4T.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View v) {

    }
}
/*
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(ConversacionLive4T.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ConversacionLive4T.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    photoCameraIntent();
                }
                break;
        }
    }

    private void photoCameraIntent(){
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto+"camera.jpg");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(ConversacionLive4T.this,
                BuildConfig.APPLICATION_ID + ".provider",    @Override
    public void onItemClick(int position) {

    }
                filePathImageCamera);
        it.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(it, IMAGE_CAMERA_REQUEST);
    }

    /**
     * Enviar foto pela galeria

    private void photoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }


      Enviar msg de texto simples para chat

    private void sendMessageFirebase(){
       // ChatModel model = new ChatModel(userModel,edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"",null);
       // mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);
        //edMessage.setText(null);
    }

/*

 */
    //@Override
//    public void onClick(View v) {

  //  }

    /*


    @Override
    public void clickImageChat(View view, int position, String nameUser, String urlPhotoUser, String urlPhotoClick) {

    }

    @Override
    public void clickImageMapChat(View view, int position, String latitude, String longitude) {

    }


}*/
