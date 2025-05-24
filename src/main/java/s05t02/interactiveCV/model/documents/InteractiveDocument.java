package s05t02.interactiveCV.model.documents;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface InteractiveDocument {
    default void printToPdf() {
    }

    default void exportToPublicView() {
    }

    String getId();
}
