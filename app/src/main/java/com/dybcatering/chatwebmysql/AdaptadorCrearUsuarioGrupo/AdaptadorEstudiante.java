package com.dybcatering.chatwebmysql.AdaptadorCrearUsuarioGrupo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dybcatering.chatwebmysql.AdaptadorGrupos.ItemGrupo;
import com.dybcatering.chatwebmysql.R;

import java.util.ArrayList;


public class AdaptadorEstudiante extends RecyclerView.Adapter<AdaptadorEstudiante.UsuarioViewHolder> {
		private Context mContext;
		private ArrayList<Object> mExampleList;
		private OnItemClickListener mListener;



	public AdaptadorEstudiante(Context context, ArrayList<Object> exampleList) {
		mContext = context;
		mExampleList = exampleList;
	}

	public void setOnClickItemListener(OnItemClickListener listener) {
		mListener = listener;

	}

	@NonNull
	@Override
	public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_usuarios, parent, false);
		return new UsuarioViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
		ItemUsuario mismoitem = (ItemUsuario) mExampleList.get(position);

		String nombregrupo =mismoitem.getNombre();

		holder.mNombreUsuario.setText(nombregrupo);
	}

	@Override
	public int getItemCount() {
		return mExampleList.size();
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public class UsuarioViewHolder extends RecyclerView.ViewHolder {


		public AppCompatCheckedTextView mNombreUsuario;


		public UsuarioViewHolder(View itemView) {
			super(itemView);
			mNombreUsuario= itemView.findViewById(R.id.checked_text_view);

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
