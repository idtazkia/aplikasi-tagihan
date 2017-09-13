package id.ac.tazkia.payment.virtualaccount.controller;

import id.ac.tazkia.payment.virtualaccount.dto.ErrorResponse;
import id.ac.tazkia.payment.virtualaccount.exception.JumlahTagihanInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(JumlahTagihanInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleJumlahTagihanInvalid(JumlahTagihanInvalidException err) {
        return new ErrorResponse("JUMLAH_TAGIHAN_INVALID", err.getMessage());
    }
}
