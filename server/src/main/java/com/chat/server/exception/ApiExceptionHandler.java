package com.chat.server.exception;

import com.chat.server.viewmodel.error.ErrorVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }

    private ResponseEntity<ErrorVm> getErrorVmResponseEntity(HttpStatus status, WebRequest request, ApiException ex) {
        String message = ex.getMessage();
        ErrorVm errorVm = new ErrorVm(status.value(), status.getReasonPhrase(), message);
        log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), status.value(), message);
        log.debug(ex.toString());
        return new ResponseEntity<>(errorVm, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVm> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return getErrorVmResponseEntity(HttpStatus.NOT_FOUND, request, ex);
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorVm> handleDuplicatedException(DuplicatedException ex, WebRequest request) {
        return getErrorVmResponseEntity(HttpStatus.CONFLICT, request, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return getErrorVmResponseEntity(HttpStatus.BAD_REQUEST, request, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorVm> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorVm errorVm = new ErrorVm(status.value(), status.getReasonPhrase(), "Request information is not valid", errors);
        return ResponseEntity.badRequest().body(errorVm);
    }
}
