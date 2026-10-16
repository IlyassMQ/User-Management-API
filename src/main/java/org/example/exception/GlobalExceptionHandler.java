
package org.example.exception;

import org.example.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(
            UserNotFound ex) {

        ErrorResponseDto error = new ErrorResponseDto(
                404,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExists(
            EmailAlreadyExists ex) {

        ErrorResponseDto error = new ErrorResponseDto(
                409,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(LoginRequired.class)
    public ResponseEntity<ErrorResponseDto> handleLoginRequired(
            LoginRequired ex
    ){
        ErrorResponseDto error = new ErrorResponseDto(
                401,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }


}
