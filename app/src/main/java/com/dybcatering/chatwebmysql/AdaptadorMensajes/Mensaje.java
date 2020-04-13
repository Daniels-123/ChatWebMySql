package com.dybcatering.chatwebmysql.AdaptadorMensajes;

public class Mensaje {

	public String MensajeEnviado;
	public String Usuario;
	public String Grupo;
	private String Enviado;
	private String TipoMensaje;

	public Mensaje() {
	}

	public Mensaje(String mensaje, String usuario, String grupo, String enviado, String tipoMensaje) {
		MensajeEnviado = mensaje;
		Usuario = usuario;
		Grupo = grupo;
		Enviado = enviado;
		TipoMensaje = tipoMensaje;
	}

	public String getTipoMensaje() {
		return TipoMensaje;
	}

	public void setTipoMensaje(String tipoMensaje) {
		TipoMensaje = tipoMensaje;
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
