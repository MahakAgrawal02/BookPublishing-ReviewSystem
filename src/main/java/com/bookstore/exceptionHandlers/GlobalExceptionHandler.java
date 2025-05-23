package com.bookstore.exceptionHandlers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * Handles various exceptions and returns consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors when @Valid fails on method arguments.
     * Extracts all field error messages and returns them with BAD_REQUEST status.
     *
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity with list of validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases when a username is not found during authentication.
     *
     * @param ex the UsernameNotFoundException
     * @return ResponseEntity with error message and NOT_FOUND status
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotFoundException(UsernameNotFoundException ex) {
        List<String> errors = Collections.singletonList("UsernameNotFoundException: " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general exceptions not explicitly caught by other handlers.
     *
     * @param ex the Exception
     * @return ResponseEntity with error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles runtime exceptions not explicitly caught by other handlers.
     *
     * @param ex the RuntimeException
     * @return ResponseEntity with error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles database constraint violations such as unique key conflicts.
     *
     * @param ex the DataIntegrityViolationException
     * @return ResponseEntity with error message and CONFLICT status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Map<String, List<String>>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        List<String> errors = Collections.singletonList("DataIntegrityViolationException: " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.CONFLICT);
    }

    /**
     * Utility method to construct error response map with a list of error messages.
     *
     * @param errors list of error messages
     * @return Map with a single key "errors" mapping to the list of messages
     */
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
