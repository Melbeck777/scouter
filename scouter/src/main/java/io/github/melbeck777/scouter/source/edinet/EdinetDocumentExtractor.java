package io.github.melbeck777.scouter.source.edinet;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts the securities report body CSV from an EDINET type=5 ZIP.
 *
 * <p>Responsibility (see ADR 0001): given the raw ZIP bytes returned by
 * {@link EdinetClient}, pick the main report CSV — the entry whose name
 * contains {@code jpcrp} — and return its text decoded as UTF-16.
 * Audit reports ({@code jpaud...}) and other entries are ignored for M0.
 *
 * <p>This class is a pure function (no network, no config): it can be unit
 * tested by feeding it a sample ZIP built from {@code sample/XBRL_TO_CSV/}.
 */
@Component
public class EdinetDocumentExtractor {

    /** Filename marker that identifies the securities report body (ADR 0001). */
    private static final String MAIN_REPORT_MARKER = "jpcrp";

    /**
     * Extract the main securities report CSV text from a type=5 ZIP.
     *
     * @param zipBytes raw bytes of the ZIP returned by the EDINET document API
     * @return the decoded text of the {@code jpcrp...} CSV entry
     */
    public String extractMainReportCsv(byte[] zipBytes) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(MAIN_REPORT_MARKER)) {
                    byte[] body = zis.readAllBytes();
                    return new String(body, StandardCharsets.UTF_16);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read EDINET ZIP", e);
        }
        throw new UnsupportedOperationException("Not found "+MAIN_REPORT_MARKER);
    }
}
