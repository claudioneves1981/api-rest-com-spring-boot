package dev.claudioneves.apirestdozero.config;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.claudioneves.apirestdozero.service.exceptions.EmailAlreadyExistsException;
import dev.claudioneves.apirestdozero.service.exceptions.UserNotFoundException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.util.ArrayList;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorModel> handleBadCredencialsException(BadCredentialsException e){

        var body = ErrorModel.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.toString())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);

    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorModel> handleEmailAlreadyExistException(EmailAlreadyExistsException e){

        var body = ErrorModel.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorModel> handleUserNotFoundException(UserNotFoundException e){
        var body = ErrorModel.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.toString())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(

            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ){

        var errors = new ArrayList<String>();

        for(FieldError error : ex.getBindingResult().getFieldErrors()){

            errors.add(error.getField() +": " + error.getDefaultMessage());

        }

        for(ObjectError error : ex.getBindingResult().getGlobalErrors()){

            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());

        }

        var body = ErrorModel.builder()
                .message(errors.toString())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);




    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(

            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request


    ){

        String message = "Query parameter '"+ ex.getParameterName()+"' is required";

        var body = ErrorModel.builder()
                .message(message)
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

    }


    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleServletRequestBindingException(

            ServletRequestBindingException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){

        var body = ErrorModel.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorModel> handleRuntimeException(RuntimeException ex){
        var body = ErrorModel.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(

            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){

        var body = ErrorModel.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorModel> handleException(Exception ex){

        var body = ErrorModel.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .build();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);

    }



}
@Data
@Builder
@JsonPropertyOrder({"error", "statusCode","message"})
class ErrorModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private String error;
    private String statusCode;
    private String message;

}
