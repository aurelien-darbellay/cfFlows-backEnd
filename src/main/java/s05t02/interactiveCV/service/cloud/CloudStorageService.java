package s05t02.interactiveCV.service.cloud;

import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;

import java.util.Map;


public interface CloudStorageService {
    Mono<Map<String, Object>> authenticateUpload(String username, Map<String, Object> body);

    Mono<Void> saveMetaData(String username, String docId, CloudMetaData metaData, PointsToFileInCloud entry);
}
