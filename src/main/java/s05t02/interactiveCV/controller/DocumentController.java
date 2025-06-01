package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DocumentCreationDto;
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
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping
    Mono<InteractiveDocument> createdNewDoc(@PathVariable("username") String username, @RequestBody DocumentCreationDto dto) {
        log.atDebug().log("Creating new document of type {}",dto.getType());
        return documentService.createDocumentInUser(username,dto.getType(),dto.getTitle()); //here maybe add error in case user don't exist
    }

    @GetMapping(DOC_ID_REL)
    Mono<InteractiveDocument> getDocById(@PathVariable("username") String username, @PathVariable("docId") String docId) {
        return documentService.getDocumentByIdInUser(username, docId);
    }

    @PostMapping(DOC_ID_REL)
    Mono<InteractiveDocument> updateDocInUser(@PathVariable("username") String username, @RequestBody InteractiveDocument updatedDoc) {
        return documentService.updateDocumentInUser(username, updatedDoc);
    }

    @PostMapping(DOC_ID_REL + DELETE_DOC_REL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> deleteDocById(@PathVariable("username") String username, @PathVariable("docId") String docId) {
        return documentService.deleteDocumentFromUser(username, docId);
    }


}
