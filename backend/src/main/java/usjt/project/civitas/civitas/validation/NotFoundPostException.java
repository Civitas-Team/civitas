package usjt.project.civitas.civitas.validation;

public class NotFoundPostException extends Exception {
    
	private static final long serialVersionUID = 1L;
	private static final String message = "O Post com o ID especificado, não foi encontrado.";
	
	public NotFoundPostException() {
		super(message);
	}
}