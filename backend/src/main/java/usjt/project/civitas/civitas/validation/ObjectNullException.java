package usjt.project.civitas.civitas.validation;

public class ObjectNullException extends Exception {

	private static final long serialVersionUID = 1L; 
	private static final String message = " deve ser fornecido!";
	
	public ObjectNullException(String objeto) {
		super(objeto + message);
	}
	
}
