package college.smart_lost_found_backend.exceptions;

import college.smart_lost_found_backend.dto.FieldErrorVM;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<RestResponse> internalAuthenticationServiceExceptionHandler(InternalAuthenticationServiceException e) {
        return new ResponseEntity<>(ResponseUtil.getUnAuthorizedResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse> badCredentialsExceptionHandler() {
        return new ResponseEntity<>(ResponseUtil.getBadRequestResponse(ErrorConstants.DEFAULT_BAD_CREDENTIALS_ERROR_MESSAGE, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<RestResponse> InsufficientAuthenticationExceptionHandler() {
        return new ResponseEntity<>(ResponseUtil.getUnAuthorizedResponse(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(ResponseUtil.getBadRequestResponse(ErrorConstants.DEFAULT_INVALID_ARGUMENT_ERROR_MESSAGE, fieldErrors), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RestResponse> noSuchElementExceptionHandler() {
        return new ResponseEntity<>(ResponseUtil.getNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        return new ResponseEntity<>(ResponseUtil.getUnAuthorizedResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> exceptionHandler() {
        return new ResponseEntity<>(ResponseUtil.getInternalServerErrorResponse(ErrorConstants.DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Invalid.class)
    public ResponseEntity<RestResponse> invalid(Invalid ex) {
        return new ResponseEntity<>(ResponseUtil.getBadRequestResponse(ex.getMessage(), ex.getDetail()), HttpStatus.BAD_REQUEST);
    }

}
