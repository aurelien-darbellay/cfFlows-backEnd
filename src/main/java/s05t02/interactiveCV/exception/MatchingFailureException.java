package s05t02.interactiveCV.exception;

public class MatchingFailureException extends Exception {
    public MatchingFailureException(String username, String docId) {
        super(String.format("No match for username %s and docId %s",username, docId));
    }
    public MatchingFailureException(String username) {
        super(String.format("No match for username %s",username));
    }
}
