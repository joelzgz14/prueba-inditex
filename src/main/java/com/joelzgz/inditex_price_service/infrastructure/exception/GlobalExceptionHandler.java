package com.joelzgz.inditex_price_service.infrastructure.exception;

import com.joelzgz.inditex_price_service.domain.exception.PriceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * Centralizes exception handling across all controllers and provides consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERROR_PRICE_NOT_FOUND = "Precio no encontrado";
    private static final String ERROR_VALIDATION = "Error de validación";
    private static final String ERROR_INVALID_REQUEST_FORMAT = "Formato de petición incorrecto";
    private static final String ERROR_INVALID_PARAMETER_TYPE = "Tipo de parámetro incorrecto";
    private static final String ERROR_MISSING_PARAMETER = "Parámetro requerido";
    private static final String ERROR_INVALID_ARGUMENT = "Argumento inválido";
    private static final String ERROR_INTERNAL_SERVER = "Error interno del servidor";

    /**
     * Handler for PriceNotFoundException.
     * Returns a 404 NOT FOUND response with details about the price not found.
     *
     * @param ex The PriceNotFoundException thrown
     * @return ResponseEntity with error details and 404 status code
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePriceNotFoundException(PriceNotFoundException ex) {
        LOGGER.warn("Price not found: {}", ex.getMessage());

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ERROR_PRICE_NOT_FOUND,
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for MethodArgumentNotValidException.
     * Returns a 400 BAD REQUEST response with details about validation errors.
     *
     * @param ex The MethodArgumentNotValidException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorDetail = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        LOGGER.warn("Validation error: {}", errorDetail);

        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_VALIDATION,
                errorDetail
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for ConstraintViolationException.
     * Returns a 400 BAD REQUEST response with validation constraint violation details.
     *
     * @param ex The ConstraintViolationException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorDetail = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        LOGGER.warn("Constraint violation: {}", errorDetail);

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_VALIDATION,
                errorDetail
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for HttpMessageNotReadableException.
     * Returns a 400 BAD REQUEST response when request body is malformed.
     *
     * @param ex The HttpMessageNotReadableException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.warn("Message not readable: {}", ex.getMessage());

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_INVALID_REQUEST_FORMAT,
                "El cuerpo de la petición no tiene el formato esperado"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for MethodArgumentTypeMismatchException.
     * Returns a 400 BAD REQUEST response when a parameter type is incorrect.
     *
     * @param ex The MethodArgumentTypeMismatchException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        String errorDetail = "El parámetro '" + ex.getName() + "' debería ser de tipo " + requiredType;

        LOGGER.warn("Type mismatch: {}", errorDetail);

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_INVALID_PARAMETER_TYPE,
                errorDetail
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for MissingServletRequestParameterException.
     * Returns a 400 BAD REQUEST response when a required parameter is missing.
     *
     * @param ex The MissingServletRequestParameterException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorDetail = "El parámetro '" + ex.getParameterName() + "' es obligatorio";

        LOGGER.warn("Missing parameter: {}", errorDetail);

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_MISSING_PARAMETER,
                errorDetail
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for IllegalArgumentException.
     * Returns a 400 BAD REQUEST response for invalid arguments.
     *
     * @param ex The IllegalArgumentException thrown
     * @return ResponseEntity with error details and 400 status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.warn("Illegal argument: {}", ex.getMessage());

        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_INVALID_ARGUMENT,
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for all other exceptions.
     * Returns a 500 INTERNAL SERVER ERROR response.
     * Logs the full stack trace for internal server errors.
     *
     * @param ex The Exception thrown
     * @return ResponseEntity with error details and 500 status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
        // Log full stack trace for unexpected errors
        LOGGER.error("Unexpected error occurred", ex);

        // Don't include detailed exception message in response to avoid leaking sensitive information
        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ERROR_INTERNAL_SERVER,
                "Se ha producido un error interno. Por favor, contacte con el administrador del sistema."
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}