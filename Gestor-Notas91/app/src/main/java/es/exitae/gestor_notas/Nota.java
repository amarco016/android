package es.exitae.gestor_notas;

import java.io.Serializable;


public class Nota implements Serializable{
	 int ID;
	String titulo;
	String descripcion;
	double latitud;
	double longitud;
	int imagen;

	
	public Nota(int ID,String titulo, String descripcion, double latitud, double longitud,int imagen){
		super();
		this.titulo=titulo;
		this.descripcion=descripcion;
		this.latitud=latitud;
		this.longitud=longitud;
		this.imagen=imagen;
		this.ID=ID;
		
	}
	
	public Nota(int ID,String titulo, String descripcion, double latitud, double longitud){
		super();
		this.titulo=titulo;
		this.descripcion=descripcion;
		this.latitud=latitud;
		this.longitud=longitud;
		this.ID=ID;
		
	}

	public String getTitulo() {
		return titulo;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getID() {
		return ID;
	}

	
	

}
