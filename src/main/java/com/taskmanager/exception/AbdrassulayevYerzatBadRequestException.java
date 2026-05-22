package com.taskmanager.exception;

import lombok.Getter;

@Getter
public class AbdrassulayevYerzatBadRequestException extends RuntimeException {

    public AbdrassulayevYerzatBadRequestException(String message) {
        super(message);
    }
}