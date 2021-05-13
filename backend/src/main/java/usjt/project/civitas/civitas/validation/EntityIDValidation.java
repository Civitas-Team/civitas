package usjt.project.civitas.civitas.validation;

public class EntityIDValidation {
    
    public static void validateID(String idParam) throws NumberFormatException {
        try {
            Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("O ID especificado não é um Long valido.");
        }
    }
}
