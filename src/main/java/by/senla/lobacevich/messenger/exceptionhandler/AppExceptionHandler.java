package by.senla.lobacevich.messenger.exceptionhandler;

import by.senla.lobacevich.messenger.dto.response.ErrorDto;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getClass().getSimpleName()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorDto> handleDataBaseException(InvalidDataException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error(errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationException(AccessDeniedException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getClass().getSimpleName()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}
