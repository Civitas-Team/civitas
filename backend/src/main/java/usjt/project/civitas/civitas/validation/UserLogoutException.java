package usjt.project.civitas.civitas.validation;

public class UserLogoutException extends Exception {

	private static final long serialVersionUID = 1L; 
	private static final String message = "Erro ao tentar realizar o logout do usuário!";

	public UserLogoutException() {
		super(message);
	}
	
}
