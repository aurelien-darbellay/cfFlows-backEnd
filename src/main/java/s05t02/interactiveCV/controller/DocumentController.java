package s05t02.interactiveCV.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DocumentCreationDto;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.service.entities.InteractiveDocumentService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(DOC_PATH)
public class DocumentController {
    private final InteractiveDocumentService documentService;
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<InteractiveDocument> createdNewDoc(@Valid @RequestBody DocumentCreationDto dto) {
        log.atDebug().log("Creating new document of type {}", dto.getType());
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> documentService.createDocumentInUser(username, dto.getType(), dto.getTitle()));
    }

    @GetMapping(DOC_ID_REL)
    Mono<InteractiveDocument> getDocById(@PathVariable("docId") String docId, @RequestParam(value = "targetUser", required = false) String targetUser) {
        return ControllerUtils.resolveUserOrAdminOverride(
                targetUser,
                username -> documentService.getDocumentByIdInUser(username, docId));
    }

    @PostMapping(DOC_ID_REL)
    Mono<InteractiveDocument> updateDocInUser(@RequestBody InteractiveDocument updatedDoc) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> documentService.updateDocumentInUser(username, updatedDoc));
    }

    @PostMapping(DOC_ID_REL + DELETE_DOC_REL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> deleteDocById(@PathVariable("docId") String docId) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> documentService.deleteDocumentFromUser(username, docId));
    }
}
