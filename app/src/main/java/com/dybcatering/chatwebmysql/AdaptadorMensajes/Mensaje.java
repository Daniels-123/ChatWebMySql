package com.dybcatering.chatwebmysql.AdaptadorMensajes;

public class Mensaje {

	public String MensajeEnviado;
	public String Usuario;
	public String Grupo;
	private String Enviado;

	public Mensaje() {
	}

	public Mensaje(String mensaje, String usuario, String grupo, String enviado) {
		MensajeEnviado = mensaje;
		Usuario = usuario;
		Grupo = grupo;
		Enviado = enviado;
	}


	public String getUsuario() {
		return Usuario;
	}

	public void setUsuario(String usuario) {
		Usuario = usuario;
	}

	public String getGrupo() {
		return Grupo;
	}

	public void setGrupo(String grupo) {
		Grupo = grupo;
	}

	public String getEnviado() {
		return Enviado;
	}

	public void setEnviado(String enviado) {
		Enviado = enviado;
	}

	public String getMensajeEnviado() {
		return MensajeEnviado;
	}

	public void setMensajeEnviado(String mensajeEnviado) {
		MensajeEnviado = mensajeEnviado;
	}
}
