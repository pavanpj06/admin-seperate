package com.example.admin.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class AppExceptionHandler {

	
	
	@ExceptionHandler(value =MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
	
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            errorResponse.put("error", "Invalid value for field '" + ife.getPath().get(0).getFieldName() + "': " + ife.getValue());
        } else {
            errorResponse.put("error", "Invalid request body");
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	

	@ExceptionHandler(AppException.class)
    public ResponseEntity<CustomErrorResponse> handleAppException(AppException ex) {
		CustomErrorResponse  error = new CustomErrorResponse (ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

	
	/*
	 * @ExceptionHandler(value = Exception.class) public
	 * ResponseEntity<AppException> handleException(String msg){ AppException
	 * appex=new AppException();
	 * 
	 * appex.setExCode("EC001"); appex.setExDesc(msg);
	 * appex.setTimeAndDate(LocalDateTime.now());
	 * 
	 * 
	 * return new ResponseEntity<AppException>(appex,
	 * HttpStatus.INTERNAL_SERVER_ERROR); }
	 */
	
	
}
