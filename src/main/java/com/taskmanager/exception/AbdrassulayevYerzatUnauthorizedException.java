package com.taskmanager.exception;

import lombok.Getter;

@Getter
public class AbdrassulayevYerzatUnauthorizedException extends RuntimeException {

    public AbdrassulayevYerzatUnauthorizedException(String message) {
        super(message);
    }
}