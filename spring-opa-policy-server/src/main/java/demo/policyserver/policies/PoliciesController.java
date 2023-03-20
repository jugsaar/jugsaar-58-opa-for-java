package demo.policyserver.policies;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exposes a dynamically generated OPA policy bundle to be consumed by policy agent side-cars.
 */
@Slf4j
@RestController
@RequestMapping("/service/v1/policies")
public class PoliciesController {

    @GetMapping("/current/bundle.tar.gz")
    public void currentPolicyBundle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        var userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        var remoteIp = request.getRemoteAddr();
        log.info("Fetch current bundle from Client: {} IP: {}", userAgent, remoteIp);

        response.setStatus(200);
        response.setContentType("application/gzip");
        response.setHeader("Content-disposition", "attachment; filename=bundle.tar.gz");
        response.setContentLength(-1);

        try (var fOut = response.getOutputStream(); //
             var buffOut = new BufferedOutputStream(fOut);  //
             var gzOut = new GzipCompressorOutputStream(buffOut);  //
             var tOut = new TarArchiveOutputStream(gzOut)) {

            // TODO compute revision from file contents
            var manifest = """
                    {
                      "revision" : "1",
                      "roots": ["authz"]
                    }
                    """;

            var manifestBytes = manifest.getBytes(StandardCharsets.UTF_8);
            var tarEntryManifest = new TarArchiveEntry(".manifest", true);
            tarEntryManifest.setSize(manifestBytes.length);
            tOut.putArchiveEntry(tarEntryManifest);
            tOut.write(manifestBytes);
            tOut.closeArchiveEntry();


            var opaPath = Paths.get("opa");
            try (var filePaths = Files.walk(opaPath,10)) {
                filePaths.forEach(path -> {
                    var file = path.toFile();
                    if (file.isDirectory()){
                        return;
                    }
                    var tarEntry = new TarArchiveEntry(file, opaPath.relativize(path).toString());
                    try {
                        tOut.putArchiveEntry(tarEntry);
                        Files.copy(path, tOut);
                        tOut.closeArchiveEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            tOut.finish();
        }
    }
}
