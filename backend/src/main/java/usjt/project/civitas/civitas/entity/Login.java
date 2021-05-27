package usjt.project.civitas.civitas.entity;

import java.io.Serializable;

public class Login implements Serializable{

	private static final long serialVersionUID = 1L;
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
