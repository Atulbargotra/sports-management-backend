package com.atul.sportsmanagement.exceptions;

import com.atul.sportsmanagement.dto.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
//        String errorMessageDescription = ex.getLocalizedMessage();
//        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
//        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
//        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(value = {AlreadyJoinedException.class})
    public ResponseEntity<Object> handleAlreadyJoinedException(AlreadyJoinedException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {NoEmptyPlaceException.class})
    public ResponseEntity<Object> handleNoEmptyPlaceException(NoEmptyPlaceException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotParticipatedException.class})
    public ResponseEntity<Object> handleNotParticipatedException(NotParticipatedException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {WinnersNotDeclaredException.class})
    public ResponseEntity<Object> handleWinnersNotDeclaredException(WinnersNotDeclaredException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {EventNotFoundException.class})
    public ResponseEntity<Object> handleEventNotFoundException(EventNotFoundException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SpringSportsException.class})
    public ResponseEntity<Object> handleSpringSportsException(SpringSportsException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUserNameNotFoundException(UsernameNotFoundException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {DisabledException.class})
    public ResponseEntity<Object> handleDisabledExceptionException(DisabledException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsExceptionException(BadCredentialsException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = ex.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDescription);
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
