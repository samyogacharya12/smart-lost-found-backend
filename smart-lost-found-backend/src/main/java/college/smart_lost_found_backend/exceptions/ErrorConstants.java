package college.smart_lost_found_backend.exceptions;

public class ErrorConstants {
    public static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong. Please Try again later";
    public static final String DEFAULT_BAD_CREDENTIALS_ERROR_MESSAGE = "Failed to sign in! Please check your credentials and try again.";
    public static final String DEFAULT_NOT_FOUND_ERROR_MESSAGE = "The resource that you are attempting to access does not exist or you don't have the necessary permissions to view it.";
    public static final String DEFAULT_UNAUTHORIZED_ERROR_MESSAGE = "You don't have the necessary permissions to view this resource.";
    public static final String DEFAULT_INVALID_ARGUMENT_ERROR_MESSAGE = "Method argument not valid";
    public static final String DEFAULT_CONCURRENCY_FAILURE_ERROR_MESSAGE = "Another user modified this data at the same time as you. Your changes were rejected.";

    private ErrorConstants() {
    }
}
