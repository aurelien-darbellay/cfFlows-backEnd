package s05t02.interactiveCV.service.cloud;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CloudinaryMetaData implements CloudMetaData {
   private final String id;
   private final String publicUrl;
}
