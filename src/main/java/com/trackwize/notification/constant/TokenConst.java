package com.trackwize.notification.constant;

public class TokenConst {

    public static final String ACCESS_TOKEN_NAME = "auth_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";

    public static final int RESET_PASSWORD_TOKEN_EXPIRY = 10; // 10min
    public static final int ACCESS_TOKEN_EXPIRY = 15; // 15min
    public static final int REFRESH_TOKEN_EXPIRY = 30 * 24 * 60 ; // 30 days

    public static final int BASE_TOKEN_EXPIRY = 60;
    public static final String TOKEN_PATH = "/";
}
