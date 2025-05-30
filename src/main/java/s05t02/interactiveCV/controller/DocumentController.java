package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.interfaces.Dto;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.InteractiveDocumentType;
import s05t02.interactiveCV.service.entities.InteractiveDocumentService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(DOC_PATH)
public class DocumentController {
    private final InteractiveDocumentService documentService;

    @PostMapping
    Mono<InteractiveDocument> createdNewDoc(@PathVariable String username, @RequestParam InteractiveDocumentType type) {
        return documentService.createDocumentInUser(username,type); //here maybe add error in case user don't exist
    }

    @GetMapping(DOC_ID_REL)
    Mono<InteractiveDocument> getDocById(@PathVariable String username, @PathVariable String docId) {
        return documentService.getDocumentByIdInUser(username, docId);
    }

    @PostMapping(DOC_ID_REL)
    Mono<InteractiveDocument> updateDocInUser(@PathVariable String username, @RequestBody InteractiveDocument updatedDoc) {
        return documentService.updateDocumentInUser(username, updatedDoc);
    }

    @PostMapping(DOC_ID_REL + DELETE_DOC_REL)
    Mono<Void> deleteDocById(@PathVariable String username, @PathVariable String docId) {
        return documentService.deleteDocumentFromUser(username, docId);
    }


}
