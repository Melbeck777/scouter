package io.github.melbeck777.scouter.source.edinet;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EdinetDocumentExtractor}.
 *
 * <p>The extractor is a pure function (no network, no Spring), so tests build a
 * small in-memory ZIP with {@link #zipOf} and feed its bytes directly. Entry
 * contents are written as UTF-16 (Java emits big-endian + BOM); the extractor
 * decodes via {@code StandardCharsets.UTF_16}, so it detects endianness from the
 * BOM regardless.
 */
class EdinetDocumentExtractorTest {

    private final EdinetDocumentExtractor extractor = new EdinetDocumentExtractor();

    @Test
    void returnsMainReportAndSkipsAudit() {
        // Audit entry first, main report second: proves the loop skips jpaud
        // and keeps scanning until it finds jpcrp.
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("XBRL_TO_CSV/jpaud-aar-cn-001.csv", "\"監査報告書\"\t\"本文\"");
        entries.put("XBRL_TO_CSV/jpcrp030000-asr-001.csv", "\"要素ID\"\t\"項目名\"");

        String csv = extractor.extractMainReportCsv(zipOf(entries));

        assertTrue(csv.contains("要素ID"));
        assertTrue(csv.contains("項目名"));
        assertFalse(csv.contains("監査報告書"));
        assertFalse(csv.contains("本文"));
    }

    @Test
    void notFoundReport() {
        // Audit entry first, main report second: proves the loop skips jpaud
        // and keeps scanning until it finds jpcrp.
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("XBRL_TO_CSV/jpaud-aar-cn-001.csv", "\"監査報告書\"\t\"本文\"");

        assertThrows(
                UnsupportedOperationException.class,
                () -> extractor.extractMainReportCsv(zipOf(entries))
        );

    }


    /**
     * Build an in-memory ZIP from {@code name -> content} pairs. Each content
     * string is stored as UTF-16 bytes, mimicking an EDINET type=5 archive.
     * Iteration order follows the map, so a LinkedHashMap fixes entry order.
     */
    private static byte[] zipOf(Map<String, String> entries) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (Map.Entry<String, String> e : entries.entrySet()) {
                zos.putNextEntry(new ZipEntry(e.getKey()));
                zos.write(e.getValue().getBytes(StandardCharsets.UTF_16));
                zos.closeEntry();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return out.toByteArray();
    }
}
