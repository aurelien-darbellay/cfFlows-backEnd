package s05t02.interactiveCV.model.documents;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "public_views")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public abstract class InteractiveDocument {

    @Id
    private String id;

    public abstract String getId();

    void getProjectedDocument() {

    }

}
