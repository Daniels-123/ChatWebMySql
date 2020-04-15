package com.dybcatering.chatwebmysql.AdaptadorMensajes;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

			case "4":
				holder.mImagen.setVisibility(View.VISIBLE);
				holder.mMensaje.setVisibility(View.GONE);
				Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png").fit().centerCrop()
						.into(holder.mImagen);

				break;

			case "3":
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
				if (mismoitem.getUsuario().equals("Daniel")) {
					holder.mImagen.setVisibility(View.GONE);
					holder.mMensaje.setVisibility(View.VISIBLE);

					holder.mNombreUsuario.setGravity(Gravity.RIGHT);
					holder.mMensaje.setGravity(Gravity.RIGHT);
					holder.mFecha.setGravity(Gravity.RIGHT);
					holder.mFecha.setTextColor(Color.BLUE);
					holder.mNombreUsuario.setText(usuario);
					holder.mMensaje.setText(mensaje);
					holder.mFecha.setText(horaenviado);
				} else {
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

	public class GrupoViewHolder extends RecyclerView.ViewHolder {


		public TextView mNombreUsuario, mMensaje, mFecha, mNombreDoc, mNombrePdf;
		public ImageView mImagen;
		private LinearLayout mDocumentoWord, mDocumentoPDF;

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
			//mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
			//mTextViewDescription = itemView.findViewById(R.id.text_view_description);
			//mBarrio = itemView.findViewById(R.id.text_barrio);
			//mVistas = itemView.findViewById(R.id.text_view_vistas);

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
	}



}
