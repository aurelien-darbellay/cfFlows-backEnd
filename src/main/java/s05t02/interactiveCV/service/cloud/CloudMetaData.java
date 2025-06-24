package s05t02.interactiveCV.service.cloud;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CloudinaryMetaData.class, name = "CLOUDINARYMETADATA"),
})
public interface CloudMetaData {
    String getId();

    String getPublicUrl();
}
