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

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
		
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<Map<String, List<String>>> handleNotFoundException(UsernameNotFoundException ex) {
		
	    List<String> errors = Collections.singletonList("UsernameNotFoundException: " + ex.getMessage());
	    return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
		
	    List<String> errors = Collections.singletonList(ex.getMessage());
	    return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
		
	    List<String> errors = Collections.singletonList(ex.getMessage());
	    return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<Map<String, List<String>>> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex ) {
		
		List<String> errors = Collections.singletonList("DataIntegrityViolationException: " + ex.getMessage());
	    return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.CONFLICT);
		
	}

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
    	
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
