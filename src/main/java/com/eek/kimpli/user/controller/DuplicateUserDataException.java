package com.eek.kimpli.user.controller;

public class DuplicateUserDataException extends RuntimeException {
    public DuplicateUserDataException(String message) {
        super(message);
    }
}
