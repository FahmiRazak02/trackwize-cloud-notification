package com.trackwize.notification.constant;

public class ErrorConst {

//    GENERAL ERROR CODE - 10001 to 19999

    public static final String GENERAL_ERROR_CODE = "10001";
    public static final String GENERAL_ERROR_MSG = "Error has occurred, please try again.";

    public static final String INVALID_INPUT_CODE = "10002";
    public static final String INVALID_INPUT_MSG = "Invalid input";

    public static final String INTERNAL_SERVER_ERROR_CODE = "10003";
    public static final String INTERNAL_SERVER_ERROR_MSG = "Internal server error";

    public static final String SQL_ERROR_CODE = "10004";
    public static final String SQL_ERROR_MSG = "Database error occurred";

    public static final String NOT_FOUND_ERROR_CODE = "10005";
    public static final String NOT_FOUND_ERROR_MSG = "Requested resource not found";

    public static final String NO_RECORD_FOUND_CODE = "10006";
    public static final String NO_RECORD_FOUND_MSG = "No Record found.";

    public static final String MISSING_REQUIRED_INPUT_CODE = "10007";
    public static final String MISSING_REQUIRED_INPUT_MSG = "Missing required input";

    public static final String SQL_EXCEPTION_CODE = "10008";
    public static final String SQL_EXCEPTION_MSG = "SQL Exception has occur, please try again.";


//    AUTHENTICATION ERROR CODE - 20001 to 29999

    public static final String AUTHENTICATION_ERROR_CODE = "20001";
    public static final String AUTHENTICATION_ERROR_MSG = "Authentication error";

    public static final String INVALID_CREDENTIALS_CODE = "20002";
    public static final String INVALID_CREDENTIALS_MSG = "Invalid credentials";

    public static final String UNAUTHORIZED_ACCESS_CODE = "20003";
    public static final String UNAUTHORIZED_ACCESS_MSG = "Unauthorized access";

    public static final String TOKEN_EXPIRED_CODE = "20004";
    public static final String TOKEN_EXPIRED_MSG = "Token has expired";

    public static final String ACCOUNT_LOCKED_CODE = "20005";
    public static final String ACCOUNT_LOCKED_MSG = "Account is locked";

    public static final String INSUFFICIENT_PERMISSIONS_CODE = "20006";
    public static final String INSUFFICIENT_PERMISSIONS_MSG = "Insufficient permissions";

    public static final String SESSION_EXPIRED_CODE = "20007";
    public static final String SESSION_EXPIRED_MSG = "Session has expired";

    public static  final String GENERATE_TOKEN_ERROR_CODE = "20008";
    public static  final String GENERATE_TOKEN_ERROR_MSG = "Token generation failed. ";

    public static  final String PERSIST_TOKEN_ERROR_CODE = "20009";
    public static  final String PERSIST_TOKEN_ERROR_MSG = "Persisting token to database, failed. ";

    public static final String USER_ALREADY_LOGGED_IN_CODE = "20010";
    public static final String USER_ALREADY_LOGGED_IN_MSG = "User is already logged in. Please log out before logging in again.";

    public static final String MISSING_REFRESH_TOKEN_CODE = "20011";
    public static final String MISSING_REFRESH_TOKEN_MSG = "Missing refresh token";

    public static final String INVALID_REFRESH_TOKEN_CODE = "20012";
    public static final String INVALID_REFRESH_TOKEN_MSG = "Refresh token is invalid";


    //    NOTIFICATION ERROR CODE - 30001 to 39999

    public static final String NOTIFICATION_ERROR_CODE = "30001";
    public static final String NOTIFICATION_ERROR_MSG = "Notification error";

    public static final String INVALID_TYPE_CODE = "30002";
    public static final String INVALID_TYPE_MSG = "Invalid Notification Type";

}
