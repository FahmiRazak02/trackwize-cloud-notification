package com.trackwize.cloud.notification.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUtil {

    private boolean result;
    private String code;
    private String msg;
    private String timestamp;
    private Object data;

    public static final String SUCCESS = "00001";
    public static final String SUCCESS_MSG = "Success";

    public static final String FAILED = "10001";
    public static final String FAILED_MSG = "Failed!";

    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
    }

    public static ResponseUtil failure() {
        return ResponseUtil
                .builder()
                .result(false)
                .code(FAILED)
                .timestamp(getCurrentTime())
                .msg(FAILED_MSG)
                .data(null)
                .build();
    }

    public static ResponseUtil success() {
        return ResponseUtil
                .builder()
                .result(true)
                .code(SUCCESS)
                .timestamp(getCurrentTime())
                .msg(SUCCESS_MSG)
                .data(null)
                .build();
    }

    public static ResponseUtil createErrorResponse(
            String errorCode,
            String errorMsg
    ) {
        ResponseUtil response = failure();
        response.setCode(errorCode);
        response.setMsg(errorMsg);
        return response;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            return "{ \"error\": \"Failed to serialize ApiResponse\" }";
        }
    }

}
