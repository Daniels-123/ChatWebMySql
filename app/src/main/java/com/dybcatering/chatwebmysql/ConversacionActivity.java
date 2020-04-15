package com.dybcatering.chatwebmysql;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private ImageView seleccionar;
    Uri pdfUri;

    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
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
        seleccionar = findViewById(R.id.btnsubir);
        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ConversacionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    selectPDF();



                }
                else
                {
                    ActivityCompat.requestPermissions(ConversacionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9 );
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {


            selectPDF();

            if (pdfUri!= null){
                uploadFile(pdfUri);
            }else{
                Toast.makeText(ConversacionActivity.this, "Por Favor seleccione un archivo", Toast.LENGTH_SHORT).show();
            }

        }else
        {
            Toast.makeText(this, "Por favor acepte los permisos", Toast.LENGTH_SHORT).show();
        }


    }

    private void selectPDF() {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(this, "archivo seleccionado: "+ data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();

                uploadFile(pdfUri);


        } else {
            Toast.makeText(this, "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show();

        }


    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Subiendo Archivo...");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();

        storageReference.child("Subidas").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                // String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                if(downloadUri.isSuccessful()){
                    String generatedFilePath = downloadUri.getResult().toString();
                    System.out.println("## Stored path is "+generatedFilePath);
                    Toast.makeText(ConversacionActivity.this, "el path es "+ generatedFilePath, Toast.LENGTH_SHORT).show();

                    //    DatabaseReference reference= database.getReference().child("files");
                    DatabaseReference reference= database.getReference("Subidas");

                    reference.child(fileName).setValue(generatedFilePath).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ConversacionActivity.this, "Archivo subido", Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(ConversacionActivity.this, "No se pudo subir el archivo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                }else{

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ConversacionActivity.this, "No se pudo subir el archivo", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
                if (currentProgress == 100){
                    progressDialog.dismiss();
                }
            }
        });

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
        Mensaje mismoitem = (Mensaje) mItemMensajes.get(position);

        String tipo = mismoitem.getTipoMensaje();

        switch (tipo){

            case "1":
                Toast.makeText(this, "mensaje es" + mismoitem.getMensajeEnviado(), Toast.LENGTH_SHORT).show();

            break;

            case "2":
                String url3 = mismoitem.getMensajeEnviado();
                DownloadManager.Request request3 = new DownloadManager.Request(Uri.parse(url3));

                request3.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                request3.setTitle(("Documento PDF")+System.currentTimeMillis());
                request3.setDescription("Descargando archivo del servidor");

                request3.allowScanningByMediaScanner();
                request3.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request3.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

                DownloadManager manager3 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager3.enqueue(request3);

            break;

            case "3":
                String url = mismoitem.getMensajeEnviado();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(("Documento Word")+System.currentTimeMillis());
                request.setDescription("Descargando archivo del servidor");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                break;

            case "4":
                String url2 = mismoitem.getMensajeEnviado();
                DownloadManager.Request request2 = new DownloadManager.Request(Uri.parse(url2));

                request2.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                request2.setTitle(("Descarga Liv4T")+System.currentTimeMillis());
                request2.setDescription("Descargando documento word del servidor");

                request2.allowScanningByMediaScanner();
                request2.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request2.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

                DownloadManager manager2 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager2.enqueue(request2);


            break;

            case "5":


            break;

            default:
                Toast.makeText(this, "Mensaje de Chat", Toast.LENGTH_SHORT).show();

        }

/*
     //   if (mismoitem.getTipoMensaje().equals("1")){
       //     Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
       // }else{
            String url = mismoitem.getMensajeEnviado();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Descarga Liv4T");
            request.setDescription("Descargando archivo del servidor");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            Toast.makeText(this, "eurkea", Toast.LENGTH_SHORT).show();
        //}/*/
    }

    private void startDownload() {

    }
}
