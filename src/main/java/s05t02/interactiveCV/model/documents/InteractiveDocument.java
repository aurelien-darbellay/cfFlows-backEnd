package s05t02.interactiveCV.model.documents;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface InteractiveDocument {
    public abstract String getTitle();

    public abstract <T extends InteractiveDocument> T getProjectedDocument();

    public abstract String getId();
}
