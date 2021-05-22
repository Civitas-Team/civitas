package usjt.project.civitas.civitas.validation;

public class InvalidEntityFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Formato da entidade incorreto!";

	public InvalidEntityFormatException() {
		super(message);
	}

}
