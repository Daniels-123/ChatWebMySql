package com.dybcatering.chatwebmysql.AdaptadorMensajes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.R;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class AdaptadorMensajes extends RecyclerView.Adapter<AdaptadorMensajes.GrupoViewHolder> {
		private Context mContext;
		private ArrayList<Object> mExampleList;
		private OnItemClickListener mListener;
	public static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss zXXX yyyy", Locale.US);
	private Object Context;
	//public static SimpleDateFormat sdfResult = new SimpleDateFormat("HH:mm:ss", Locale.US);
	//public static SimpleDateFormat sdfResultMinutos = new SimpleDateFormat("m", Locale.US);


	public AdaptadorMensajes(Context context, ArrayList<Object> exampleList) {
		mContext = context;
		mExampleList = exampleList;
	}

	public void setOnClickItemListener(OnItemClickListener listener) {
		mListener = listener;

	}

	@NonNull
	@Override
	public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_rv_mensajes, parent, false);
		return new GrupoViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
		Mensaje mismoitem = (Mensaje) mExampleList.get(position);

		String mensaje = mismoitem.getMensajeEnviado();
		String usuario = mismoitem.getUsuario();
		String grupo = mismoitem.getGrupo();
		String horaenviado = mismoitem.getEnviado();
		String tipomensaje = mismoitem.getTipoMensaje();
		Date currentTime = Calendar.getInstance().getTime();


		switch (tipomensaje) {

			case "5":
				holder.mDocumentoPDF.setVisibility(View.GONE);
				holder.mDocumentoWord.setVisibility(View.GONE);
                holder.mImagen.setVisibility(View.GONE);
				holder.mAudio.setVisibility(View.VISIBLE);
				holder.mMensaje.setVisibility(View.GONE);

				break;

			case "4":
				holder.mDocumentoPDF.setVisibility(View.GONE);
				holder.mDocumentoWord.setVisibility(View.GONE);
                holder.mAudio.setVisibility(View.GONE);
                holder.mImagen.setVisibility(View.VISIBLE);
				holder.mMensaje.setVisibility(View.GONE);
				Picasso.with(mContext).load(mismoitem.getMensajeEnviado()).fit().centerCrop()
						.into(holder.mImagen);

				break;

			case "3":

				holder.mImagen.setVisibility(View.GONE);
				holder.mDocumentoWord.setVisibility(View.VISIBLE);
				holder.mMensaje.setVisibility(View.GONE);
				holder.mNombreDoc.setText("Archivo Word");

				break;
			case "2":
				holder.mDocumentoPDF.setVisibility(View.VISIBLE);
				holder.mMensaje.setVisibility(View.GONE);
				holder.mNombrePdf.setText("Documento PDF");


				break;



			case "1":
                holder.mAudio.setVisibility(View.GONE);

                if (mismoitem.getUsuario().equals("Daniel")) {
					holder.mImagen.setVisibility(View.GONE);
					holder.mMensaje.setVisibility(View.VISIBLE);
					holder.mDocumentoPDF.setVisibility(View.GONE);
					holder.mDocumentoWord.setVisibility(View.GONE);
					holder.mNombreUsuario.setGravity(Gravity.RIGHT);
					holder.mMensaje.setGravity(Gravity.RIGHT);
					holder.mFecha.setGravity(Gravity.RIGHT);
					holder.mFecha.setTextColor(Color.BLUE);
					holder.mNombreUsuario.setText(usuario);
					holder.mMensaje.setText(mensaje);
					holder.mFecha.setText(horaenviado);
				} else {
                    holder.mImagen.setVisibility(View.GONE);
                    holder.mMensaje.setVisibility(View.VISIBLE);
                    holder.mDocumentoPDF.setVisibility(View.GONE);
                    holder.mDocumentoWord.setVisibility(View.GONE);

                    holder.mNombreUsuario.setGravity(Gravity.LEFT);
					holder.mMensaje.setGravity(Gravity.LEFT);
					holder.mFecha.setGravity(Gravity.LEFT);
					holder.mFecha.setTextColor(Color.RED);
					holder.mNombreUsuario.setText(usuario);
					holder.mMensaje.setText(mensaje);
					holder.mFecha.setText(horaenviado);

				}
				break;

		}


		/*
		if (tipomensaje.equals("4")) {
			holder.mImagen.setVisibility(View.VISIBLE);
			holder.mMensaje.setVisibility(View.GONE);
			Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png").fit().centerCrop()
					.into(holder.mImagen);

//			Picasso.with((android.content.Context) Context).load("http://i.imgur.com/DvpvklR.png").into(holder.mImagen);
		}else if (tipomensaje.equals("")) {
		} else if(tipomensaje.equals("1") && mismoitem.getUsuario().equals("Daniel")) {
				holder.mImagen.setVisibility(View.GONE);
				holder.mMensaje.setVisibility(View.VISIBLE);

				holder.mNombreUsuario.setGravity(Gravity.RIGHT);
				holder.mMensaje.setGravity(Gravity.RIGHT);
				holder.mFecha.setGravity(Gravity.RIGHT);
				holder.mFecha.setTextColor(Color.BLUE);
				holder.mNombreUsuario.setText(usuario);
				holder.mMensaje.setText(mensaje);
				holder.mFecha.setText(horaenviado);
				//holder.tvNombreUsuario.setText(listaMensajes.get(i).getUsuarioOrigen());
				//holder.tvMensaje.setText(listaMensajes.get(i).getMensaje());
			} else if (tipomensaje.equals()){
				holder.mNombreUsuario.setGravity(Gravity.LEFT);
				holder.mMensaje.setGravity(Gravity.LEFT);
				holder.mFecha.setGravity(Gravity.LEFT);
				holder.mFecha.setTextColor(Color.RED);
				holder.mNombreUsuario.setText(usuario);
				holder.mMensaje.setText(mensaje);
				holder.mFecha.setText(horaenviado);
				//holder.tvNombreUsuario.setText(listaMensajes.get(i).getUsuarioOrigen());
				//holder.tvMensaje.setText(listaMensajes.get(i).getMensaje());
			}
		}

		/*holder.mNombreUsuario.setText(usuario);
		holder.mMensaje.setText(mensaje);
		holder.mFecha.setText(horaenviado);
*/





	}

	@Override
	public int getItemCount() {
		return mExampleList.size();
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public class GrupoViewHolder extends RecyclerView.ViewHolder  implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {


		public TextView mNombreUsuario, mMensaje, mFecha, mNombreDoc, mNombrePdf;
		public ImageView mImagen;
		private LinearLayout mDocumentoWord, mDocumentoPDF;
		private RelativeLayout mAudio;
		//integracion archivo de audio en card
		private ImageButton btn_play_pause;
		private SeekBar seekBar;
		private TextView textView;

		private MediaPlayer mediaPlayer;
		private int mediaFileLength;
		private int realtimeLengh;
		final Handler handler = new Handler();



		public GrupoViewHolder(View itemView) {
			super(itemView);
			mNombreUsuario= itemView.findViewById(R.id.usuario);
			mMensaje = itemView.findViewById(R.id.mensajetexto);
			mFecha = itemView.findViewById(R.id.fechahora);
			mImagen = itemView.findViewById(R.id.imageView);
			mDocumentoWord = itemView.findViewById(R.id.documentoword);
			mNombreDoc = itemView.findViewById(R.id.nombredoc);
			mDocumentoPDF = itemView.findViewById(R.id.documentopdf);
			mNombrePdf = itemView.findViewById(R.id.nombrepdf);
			mAudio = itemView.findViewById(R.id.relativeaudio);
			//mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
			//mTextViewDescription = itemView.findViewById(R.id.text_view_description);
			//mBarrio = itemView.findViewById(R.id.text_barrio);
			//mVistas = itemView.findViewById(R.id.text_view_vistas);

			seekBar = itemView.findViewById(R.id.seekbar);
			textView = itemView.findViewById(R.id.texttimer);

			seekBar.setMax(99);
			seekBar.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (mediaPlayer.isPlaying()){
						SeekBar seekBar = (SeekBar)v;
						int playPosition = (mediaFileLength/100)*seekBar.getProgress();
						mediaPlayer.seekTo(playPosition);

					}
					return false;
				}
			});
			btn_play_pause = itemView.findViewById(R.id.btn_play_pause);

			btn_play_pause.setOnClickListener(new View.OnClickListener() {


				@Override
				public void onClick(View v) {

					//final ProgressDialog mDialog = new ProgressDialog(mContext.getApplicationContext());

					AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							Toast.makeText(mContext.getApplicationContext(), "por favor espere", Toast.LENGTH_SHORT).show();
							//		mDialog.setMessage("Por favor espere un momento");
							//mDialog.show();
						}

						@Override
						protected String doInBackground(String... strings) {
							try {
								mediaPlayer.setDataSource(strings[0]);
								mediaPlayer.prepare();
							}
							catch (Exception ex){

							}
							return  "";



						}

						@Override
						protected void onPostExecute(String s) {
							mediaFileLength = mediaPlayer.getDuration();
							realtimeLengh = mediaFileLength;
							if (!mediaPlayer.isPlaying()){
								mediaPlayer.start();
								btn_play_pause.setImageResource(R.drawable.ic_stop);
							}else{
								mediaPlayer.pause();
								btn_play_pause.setImageResource(R.drawable.ic_play);
							}

							updateSeekBar();
//							mDialog.dismiss();
						}

					};
					Mensaje mismoitem = (Mensaje) mExampleList.get(getAdapterPosition());

					mp3Play.execute(mismoitem.getMensajeEnviado());
					//					mp3Play.execute(mismoitem.getMensajeEnviado()"https://firebasestorage.googleapis.com/v0/b/diarios-2a7fd.appspot.com/o/Subidas%2FSubidas_1586847089260_alt%3Dmedia%26token%3D3bd4f03b-77e3-4957-88c2-6d0dafccea18.mp3%20(online-audio-converter.com).mp3?alt=media&token=acf83f0b-5fcc-472c-94a0-bb5e4fabbc12");

				}
			});

			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						int position = getAdapterPosition();
						if (position != RecyclerView.NO_POSITION) {
							mListener.onItemClick(position);
						}
					}
				}
			});
		}
		private void updateSeekBar() {
			seekBar.setProgress((int) (((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
			if (mediaPlayer.isPlaying()){
				Runnable updater = new Runnable() {
					@Override
					public void run() {
						updateSeekBar();
						realtimeLengh-=1000;
						textView.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtimeLengh),
								TimeUnit.MILLISECONDS.toSeconds(realtimeLengh) -
								TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimeLengh))));
					}
				};
				handler.postDelayed(updater, 1000);
			}
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
				seekBar.setSecondaryProgress(percent);
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			btn_play_pause.setImageResource(R.drawable.ic_play);


		}
	}




}
