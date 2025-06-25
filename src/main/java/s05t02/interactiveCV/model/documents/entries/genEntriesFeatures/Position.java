package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Position {
    @JsonProperty("xCord")
    private double xCord;
    @JsonProperty("yCord")
    private double yCord;
}
