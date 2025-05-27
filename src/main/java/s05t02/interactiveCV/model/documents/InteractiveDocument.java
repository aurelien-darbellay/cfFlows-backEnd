package s05t02.interactiveCV.model.documents;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


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
    class HtmlTargetCoordinate {
        private final String variableName;
        private final String templateName;
    }

    HtmlTargetCoordinate getHtmlTargetCoordinate();

    default List<Entry> collectAllEntries() {
        List<Entry> entries = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (Entry.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(this);
                    if (value != null) {
                        entries.add((Entry) value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access field: " + field.getName(), e);
                }
            }
        }
        return entries;
    }
}
