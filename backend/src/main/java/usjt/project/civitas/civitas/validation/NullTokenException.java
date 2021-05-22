package usjt.project.civitas.civitas.validation;

public class NullTokenException extends Exception {

	private static final long serialVersionUID = 1L; 
	private static final String message = "Formato do toke inválido";

	public NullTokenException() {
		super(message);
	}
	
}
