package usjt.project.civitas.civitas.validation;

public class NotFoundPersonException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Usuário não encontrado!";
	
	public NotFoundPersonException() {
		super(message);
	}
}