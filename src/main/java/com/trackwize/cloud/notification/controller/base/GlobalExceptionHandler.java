package com.trackwize.cloud.notification.controller.base;

import com.trackwize.cloud.notification.constant.ErrorConst;
import com.trackwize.cloud.notification.exception.TrackWizeException;
import com.trackwize.cloud.notification.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TrackWizeException.class)
    public ResponseUtil handleKWAPException(TrackWizeException e) {
        log.error("TrackWizeException occurred: {}", e.getMessage(), e);
        return ResponseUtil.createErrorResponse(
                e.getMessageCode(),
                e.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseUtil handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException occurred: {}", e.getMessage(), e);
        return ResponseUtil.createErrorResponse(
                ErrorConst.SQL_EXCEPTION_CODE,
                ErrorConst.SQL_EXCEPTION_MSG
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseUtil handleGenericException(Exception e) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return ResponseUtil.createErrorResponse(
                ErrorConst.GENERAL_ERROR_CODE,
                ErrorConst.GENERAL_ERROR_MSG
        );
    }
}
