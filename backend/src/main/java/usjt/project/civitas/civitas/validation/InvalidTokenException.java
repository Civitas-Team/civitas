package usjt.project.civitas.civitas.validation;

public class InvalidTokenException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Acesso negado!";
	
	public InvalidTokenException() {
		super(message);
	}
}
