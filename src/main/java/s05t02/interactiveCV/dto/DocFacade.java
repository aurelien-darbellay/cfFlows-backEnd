package s05t02.interactiveCV.dto;

public record DocFacade(String docId, String docTitle) {
    static public DocFacade of(String docId, String docTitle) {
        return new DocFacade(docId, docTitle);
    }
}
