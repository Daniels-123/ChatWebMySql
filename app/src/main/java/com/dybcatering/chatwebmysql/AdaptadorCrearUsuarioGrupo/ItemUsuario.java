package com.dybcatering.chatwebmysql.AdaptadorCrearUsuarioGrupo;

public class ItemUsuario {

	private String Nombre;


	public ItemUsuario() {
	}

	public ItemUsuario(String nombre) {
		Nombre = nombre;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	private boolean isChecked;

	public boolean getChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}
}
