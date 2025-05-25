package s05t02.interactiveCV.exception;

public class IllegalDocumentTypeException extends Exception {
    public IllegalDocumentTypeException() {
        super("Error: document type not recognized");
    }
}
