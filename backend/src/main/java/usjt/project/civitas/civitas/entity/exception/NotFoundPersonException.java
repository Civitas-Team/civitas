package usjt.project.civitas.civitas.entity.exception;

public class NotFoundPersonException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "Usuário não encontrado!";
	
	public NotFoundPersonException() {
		super(message);
	}
}