package s05t02.interactiveCV.model.documents;

import lombok.Getter;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Getter
public enum InteractiveDocumentType {
    INTERACTIVE_CV(InteractiveCv.class);
    private final Class<? extends InteractiveDocument> clazz;
    InteractiveDocumentType(Class<? extends InteractiveDocument> clazz){
        this.clazz = clazz;
    }
    public Mono<InteractiveDocument> createDoc() {
        try {
            InteractiveDocument newDoc = this.clazz.getDeclaredConstructor().newInstance();
            newDoc.setId(UUID.randomUUID().toString());
            return Mono.just(newDoc);
        }catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            return Mono.error(e);
        }
    }

}
