package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
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

    @PostMapping(ENTRY_ADD_REL)
    Mono<Entry> addEntryInDoc(@PathVariable("username") String username,@PathVariable("docId") String docId,@RequestBody Entry entry){
        return entryService.addEntry(username,docId,entry);
    }
    @PostMapping(ENTRY_UPDATE_REL)
    Mono<Entry> updateEntryInDoc(@PathVariable("username") String username, @PathVariable("docId") String docId, Entry updatedEntry){
        return entryService.modifyEntry(username,docId,updatedEntry);
    }
    @PostMapping(ENTRY_DELETE_REL)
    Mono<Void> deleteEntryInDoc(@PathVariable("username") String username, @PathVariable("docId") String docId, Entry entry){
        return entryService.removeEntry(username,docId,entry);
    }
}
