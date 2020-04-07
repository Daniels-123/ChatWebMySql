package com.dybcatering.chatwebmysql.AdaptadorMensajes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.R;

import java.util.ArrayList;


public class AdaptadorMensajes extends RecyclerView.Adapter<AdaptadorMensajes.GrupoViewHolder> {
		private Context mContext;
		private ArrayList<Object> mExampleList;
		private OnItemClickListener mListener;



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

		String mensaje =mismoitem.getMensajeEnviado();
		String usuario = mismoitem.getUsuario();
		String grupo = mismoitem.getGrupo();
		String horaenviado = mismoitem.getEnviado();

		holder.mNombreUsuario.setText(usuario);
		holder.mMensaje.setText(mensaje);
		holder.mFecha.setText(horaenviado);

	}

	@Override
	public int getItemCount() {
		return mExampleList.size();
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public class GrupoViewHolder extends RecyclerView.ViewHolder {


		public TextView mNombreUsuario, mMensaje, mFecha;


		public GrupoViewHolder(View itemView) {
			super(itemView);
			mNombreUsuario= itemView.findViewById(R.id.usuario);
			mMensaje = itemView.findViewById(R.id.mensaje);
			mFecha = itemView.findViewById(R.id.fechahora);

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
