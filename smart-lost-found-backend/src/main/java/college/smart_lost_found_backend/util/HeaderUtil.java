package college.smart_lost_found_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.logging.Logger;

@Slf4j
public class HeaderUtil {


    static final String APPLICATION_NAME = "smartlostfoundbackend";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    private static HttpHeaders createResponseMessageAlert(HttpHeaders headers, String message) {
        headers.add("X-" + APPLICATION_NAME + "-message", message);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        HttpHeaders headers = createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
        return createResponseMessageAlert(headers, entityName + " created successfully.");
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        HttpHeaders headers = createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
        return createResponseMessageAlert(headers, entityName + " updated successfully.");
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        HttpHeaders headers = createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
        return createResponseMessageAlert(headers, entityName + " deleted successfully.");
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }

    public static HttpHeaders createApiResponse(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", message);
        return headers;
    }
}
