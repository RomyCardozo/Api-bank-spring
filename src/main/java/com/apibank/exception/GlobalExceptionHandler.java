package com.apibank.exception;

import com.apibank.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.PessimisticLockException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // -------------------------------------------------------
    // 404 – Entity Not Found
    // Equivalent to Node.js: 404 { error: "XXX no encontrada" }
    // -------------------------------------------------------
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // -------------------------------------------------------
    // 409 – Insufficient Balance
    // Equivalent to Node.js: 409 { error: "Saldo insuficiente" }
    // -------------------------------------------------------
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientBalance(InsufficientBalanceException ex) {
        log.warn("Insufficient balance: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // -------------------------------------------------------
    // 409 – Data Integrity / FK violations
    // Equivalent to Node.js: 500 with DB error re-mapped to 409
    // -------------------------------------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO("Violación de integridad de datos: " +
                        ex.getMostSpecificCause().getMessage()));
    }

    // -------------------------------------------------------
    // 409 – Deadlock / pessimistic lock failure
    // Equivalent to Node.js: 409 { error: "Conflicto de concurrencia, por favor
    // reintente" }
    // -------------------------------------------------------
    @ExceptionHandler({ PessimisticLockException.class, ObjectOptimisticLockingFailureException.class })
    public ResponseEntity<ErrorResponseDTO> handleLockConflict(Exception ex) {
        log.warn("Lock conflict detected: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO("Conflicto de concurrencia, por favor reintente"));
    }

    // -------------------------------------------------------
    // 400 – Bean Validation failures (@Valid)
    // -------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO("Faltan campos: " + errors));
    }

    // -------------------------------------------------------
    // 400 – Malformed JSON body
    // -------------------------------------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO("El cuerpo de la solicitud es inválido o está malformado"));
    }

        // -------------------------------------------------------
        // 404 – Static resource or route not found
        // -------------------------------------------------------
        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(NoResourceFoundException ex) {
                log.info("Resource not found: {}", ex.getResourcePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponseDTO("Recurso no encontrado"));
        }

    // -------------------------------------------------------
    // 500 – Generic fallback
    // Equivalent to Node.js: 500 { error: "Error interno" }
    // -------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO("Error interno"));
    }
}
