package s05t02.interactiveCV.repository.customRepos.updatesCreators;

import org.springframework.data.mongodb.core.query.Update;

public interface CreatesMongoDbUpdate {
    public abstract Update createAddUpdate();

    public abstract Update createRemoveUpdate();
}
