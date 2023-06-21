package dev.ednascimento.articleservice.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    ProblemDetail hadlerBusinessException(BusinessException e) throws URISyntaxException {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        problemDetail.setType(new URI("http://local:8081/problems"));
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail("Erro ao buscar endPoint");
        problemDetail.setProperty("categoria", "User request");
        problemDetail.setProperty("timeStamp", LocalDateTime.now());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var mapErrors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return getResponseEntity("invalid data", mapErrors);
    }


    private ResponseEntity<Object> getResponseEntity(String message, List<FieldValidationError> detailedErrors) {

        Map<String, Object> errorResult = new HashMap<>();

        if (detailedErrors != null && !detailedErrors.isEmpty()) {
            errorResult.put("type", "http://local:8081/problems");
            errorResult.put("title", message);
            errorResult.put("status", HttpStatus.BAD_REQUEST);
            errorResult.put("detail", detailedErrors);
        }

        if (logger.isWarnEnabled())
            logger.warn(errorResult.toString());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    record FieldValidationError(String field, String detail) {
    }


}