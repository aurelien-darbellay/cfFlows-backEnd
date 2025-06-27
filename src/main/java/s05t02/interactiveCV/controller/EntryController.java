package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.service.entities.EntryService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ENTRY_BASE_PATH)
public class EntryController {

    private final EntryService entryService;
    private static final Logger log = LoggerFactory.getLogger(EntryController.class);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ENTRY_ADD_REL)
    Mono<Entry> addEntryInDoc(@PathVariable("docId") String docId, @RequestBody Entry entry) {
        log.atDebug().log("Entry name in db: {}", entry.getKeyNameInDB());
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> entryService.addEntry(username, docId, entry));
    }

    @PostMapping(ENTRY_UPDATE_REL)
    Mono<Entry> updateEntryInDoc(@PathVariable("docId") String docId, @RequestBody Entry updatedEntry) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> entryService.modifyEntry(username, docId, updatedEntry));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(ENTRY_DELETE_REL)
    Mono<Void> deleteEntryInDoc(@PathVariable("docId") String docId, @RequestBody Entry entry) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> entryService.removeEntry(username, docId, entry));
    }
}
