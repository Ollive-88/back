package org.palpalmans.ollive_back.common.exception;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(
		HttpServletRequest request, BindException bindException
	) {
		printException(bindException);
		List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();

		StringBuilder errorMessage = new StringBuilder();
		errorMessage.append("validation failed fields: [ ");

		int size = fieldErrors.size();
		for (int i = 0; i < size; i++) {
			errorMessage.append(fieldErrors.get(i).getField());
			if (i == size - 1) {
				continue;
			}
			errorMessage.append(", ");
		}
		errorMessage.append(" ]");

		return new ResponseEntity<>(
			ErrorResponse.of(errorMessage.toString(), request.getRequestURI(), fieldErrors),
			BAD_REQUEST
		);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> TokenExpiredExceptionHandler(
			HttpServletRequest request, ExpiredJwtException exception
	) {
		printException(exception);
		return new ResponseEntity<>(
				ErrorResponse.of(exception.getMessage(), request.getRequestURI()),
				UNAUTHORIZED
		);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> AccessDeniedExceptionHandler(
			HttpServletRequest request, AccessDeniedException exception
	) {
		printException(exception);
		return new ResponseEntity<>(
				ErrorResponse.of(exception.getMessage(), request.getRequestURI()),
				UNAUTHORIZED
		);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> NotFoundExceptionHandler(
		HttpServletRequest request, Exception exception
	) {
		printException(exception);
		return new ResponseEntity<>(ErrorResponse.of(
			exception.getMessage(), request.getRequestURI()),
			NOT_FOUND
		);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> runtimeExceptionHandler(
		HttpServletRequest request, RuntimeException runtimeException
	) {
		printException(runtimeException);
		return new ResponseEntity<>(ErrorResponse.of(
			runtimeException.getMessage(), request.getRequestURI()),
			INTERNAL_SERVER_ERROR
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(
		HttpServletRequest request, Exception exception
	) {
		printException(exception);
		return new ResponseEntity<>(
			ErrorResponse.of(exception.getMessage(), request.getRequestURI()),
			INTERNAL_SERVER_ERROR
		);
	}


	private void printException(Exception exception) {
		log.error("exception 발생: ", exception);
	}
}
