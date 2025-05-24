package s05t02.interactiveCV.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String identifier) {
        super(String.format("Entity with identifier %s not found in DB", identifier));
    }
}
