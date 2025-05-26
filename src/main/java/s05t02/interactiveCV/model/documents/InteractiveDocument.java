package s05t02.interactiveCV.model.documents;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface InteractiveDocument {

    String getTitle();
    <T extends InteractiveDocument> T getProjectedDocument();
    String getId();

    @Getter
    @AllArgsConstructor
    class HtmlTargetCoordinate{
        private final String variableName;
        private final String templateName;
    }
    HtmlTargetCoordinate getHtmlTargetCoordinate();
}
