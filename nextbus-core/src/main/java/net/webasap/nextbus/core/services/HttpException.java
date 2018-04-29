package net.webasap.nextbus.core.services;

import lombok.Getter;

@Getter
public class HttpException extends Exception {

    private int statusCode = -1;

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }
}
