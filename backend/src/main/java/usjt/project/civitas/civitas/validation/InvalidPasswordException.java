package usjt.project.civitas.civitas.validation;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Senha do usuário incorreta!";

	public InvalidPasswordException() {
		super(message);
	}
	
}
