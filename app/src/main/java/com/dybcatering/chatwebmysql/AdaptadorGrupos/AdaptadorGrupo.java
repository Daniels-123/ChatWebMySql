package com.dybcatering.chatwebmysql.AdaptadorGrupos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dybcatering.chatwebmysql.R;

import java.util.ArrayList;


public class AdaptadorGrupo  extends RecyclerView.Adapter<AdaptadorGrupo.GrupoViewHolder> {
	private Context mContext;
	private ArrayList<Object> mExampleList;
	private OnItemClickListener mListener;



	public AdaptadorGrupo(Context context, ArrayList<Object> exampleList) {
		mContext = context;
		mExampleList = exampleList;
	}

	public void setOnClickItemListener(OnItemClickListener listener) {
		mListener = listener;

	}

	@NonNull
	@Override
	public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.itemgrupos, parent, false);
		return new GrupoViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
		ItemGrupo mismoitem = (ItemGrupo) mExampleList.get(position);

		String nombregrupo =mismoitem.getNombre();

		holder.mNombreCurso.setText(nombregrupo);
	}

	@Override
	public int getItemCount() {
		return mExampleList.size();
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public class GrupoViewHolder extends RecyclerView.ViewHolder {


		public TextView mNombreCurso;


		public GrupoViewHolder(View itemView) {
			super(itemView);
			mNombreCurso= itemView.findViewById(R.id.titulo);

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
