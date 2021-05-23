package usjt.project.civitas.civitas.validation;

public class IDNullException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "ID Nulo!";
	
	public IDNullException() {
		super(message);
	}
	
}