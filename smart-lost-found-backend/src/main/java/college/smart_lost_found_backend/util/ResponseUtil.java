package college.smart_lost_found_backend.util;

import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.enumconstant.ResponseStatus;
import college.smart_lost_found_backend.exceptions.ErrorConstants;
import org.springframework.http.server.ServerHttpResponse;
import org.zalando.problem.DefaultProblem;

import java.util.List;

public class ResponseUtil {

    public static RestResponse getErrorResponse(DefaultProblem defaultProblem, String message) {
        if (defaultProblem.getStatus() != null && defaultProblem.getStatus().getStatusCode() == 400)
            return getBadRequestResponse(message, null);
        else if (defaultProblem.getStatus() != null && defaultProblem.getStatus().getStatusCode() == 404)
            return getNotFoundResponse();
        return getInternalServerErrorResponse(ErrorConstants.DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
    }

    public static RestResponse getNotFoundResponse() {
        return RestResponse.builder()
                .message(ErrorConstants.DEFAULT_NOT_FOUND_ERROR_MESSAGE)
                .responseStatus(ResponseStatus.RESOURCE_NOT_FOUND)
                .status(ResponseStatus.RESOURCE_NOT_FOUND.getValue())
                .build();
    }

    public static RestResponse getUnAuthorizedResponse() {
        return RestResponse.builder()
                .message(ErrorConstants.DEFAULT_UNAUTHORIZED_ERROR_MESSAGE)
                .responseStatus(ResponseStatus.UNAUTHORIZED_USER)
                .status(ResponseStatus.UNAUTHORIZED_USER.getValue())
                .build();
    }

    public static RestResponse getUnAuthorizedResponse(String message) {
        return RestResponse.builder()
                .message(message)
                .responseStatus(ResponseStatus.UNAUTHORIZED_USER)
                .status(ResponseStatus.UNAUTHORIZED_USER.getValue())
                .build();
    }

    public static RestResponse getConflictsResponse(String message) {
        return RestResponse.builder()
                .message(message)
                .responseStatus(ResponseStatus.UNAUTHORIZED_USER)
                .status(ResponseStatus.UNAUTHORIZED_USER.getValue())
                .build();
    }

    public static RestResponse getSuccessResponse(Object body, String message) {
        return RestResponse.builder()
                .message(message)
                .detail(body)
                .responseStatus(ResponseStatus.SUCCESS)
                .status(ResponseStatus.SUCCESS.getValue())
                .build();
    }

    public static RestResponse getBadRequestResponse(String message, Object detail) {
        return RestResponse.builder()
                .message(message)
                .responseStatus(ResponseStatus.BAD_REQUEST)
                .status(ResponseStatus.BAD_REQUEST.getValue())
                .build();
    }

    public static RestResponse getInternalServerErrorResponse(String message) {
        return RestResponse.builder()
                .message(message)
                .responseStatus(ResponseStatus.INTERNAL_SERVER_ERROR)
                .status(ResponseStatus.INTERNAL_SERVER_ERROR.getValue())
                .build();
    }

    public static String getResponseMessage(ServerHttpResponse response, boolean success) {
        String message = getMessageFromHeader(response);
        if (message == null) {
            if (success) message = "Success";
            else message = "Failure";
        }
        return message;
    }

    private static String getMessageFromHeader(ServerHttpResponse response) {
        List<String> messageList = response.getHeaders().get("X-" + HeaderUtil.APPLICATION_NAME + "-message");
        if (messageList != null)
            return messageList.get(0);
        return null;
    }
}
