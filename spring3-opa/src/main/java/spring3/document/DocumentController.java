package spring3.document;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
class DocumentController {

    private final DocumentFacade documentFacade;

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable long id) {
        return documentFacade.getDocumentById(id);
    }
}
