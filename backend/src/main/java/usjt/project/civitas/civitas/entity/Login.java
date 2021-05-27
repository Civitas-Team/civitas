package usjt.project.civitas.civitas.entity;

import java.io.Serializable;

import javax.persistence.Entity;


public class Login implements Serializable{
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	private String senha;

}
