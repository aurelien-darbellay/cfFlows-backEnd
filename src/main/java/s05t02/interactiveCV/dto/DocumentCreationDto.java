package s05t02.interactiveCV.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import s05t02.interactiveCV.model.documents.InteractiveDocumentType;

@Data
public class DocumentCreationDto {
    @NotBlank(message = "Title is required")
    private final String title;
    @NotBlank(message = "Type is required")
    private final InteractiveDocumentType type;

    @JsonCreator
    public DocumentCreationDto(
            @JsonProperty("title") String title,
            @JsonProperty("type") InteractiveDocumentType type
    ) {
        this.title = title;
        this.type = type;
    }
}
