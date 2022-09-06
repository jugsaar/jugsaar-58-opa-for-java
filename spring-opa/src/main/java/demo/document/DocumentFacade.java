package demo.document;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentFacade {

    private final DocumentService documentService;

    // @util.mapOf(...)
    @PostAuthorize("@opa.isAllowed('read', T(java.util.Map).of('type', 'document', 'owner', returnObject.owner))")
    public Document getDocumentById(long id) {
        return documentService.getDocumentById(id);
    }
}
