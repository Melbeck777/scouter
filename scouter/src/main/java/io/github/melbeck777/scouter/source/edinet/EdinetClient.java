package io.github.melbeck777.scouter.source.edinet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP client for the EDINET document API.
 *
 * <p>Responsibility: given a document ID, fetch the raw ZIP bytes of the
 * CSV bundle (type=5). Keep this class thin — networking only. ZIP extraction
 * and CSV parsing live in their own classes so they stay testable without a
 * network connection.
 */
@Component
public class EdinetClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;

    public EdinetClient(
            @Value("${edinet.base-url}") String baseUrl,
            @Value("${edinet.api-key}") String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.restClient = RestClient.create();
    }

    /**
     * Fetch a submitted document as a ZIP byte array (type=5 = CSV bundle).
     *
     * @param docId EDINET document ID, e.g. "S100XXXX"
     * @return raw ZIP bytes returned by the EDINET document API
     */
    public byte[] fetchDocumentZip(String docId) {
        byte[] zip = restClient.get()
            .uri("{base}/documents/{docId}?type=5&Subscription-Key={key}", baseUrl, docId, apiKey)
            .retrieve()
            .body(byte[].class);

        System.out.println(zip);
        return zip;
    }
}
