package usjt.project.civitas.civitas.entity;

import java.io.Serializable;

import javax.persistence.Entity;


public class Imagem implements Serializable{
	private String imagemBase64;

	public String getImagemBase64() {
		return imagemBase64;
	}

	public void setImagemBase64(String imagemBase64) {
		this.imagemBase64 = imagemBase64;
	}
	

}
