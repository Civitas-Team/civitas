package usjt.project.civitas.civitas.validation;

public class UserLoginException extends Exception {

	private static final long serialVersionUID = 1L; 
	private static final String message = "Erro ao tentar realizar o login do usuário! Usuário já logado.";

	public UserLoginException() {
		super(message);
	}
	
}
