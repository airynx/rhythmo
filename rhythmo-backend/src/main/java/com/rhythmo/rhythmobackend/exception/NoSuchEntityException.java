package com.rhythmo.rhythmobackend.exception;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException(Long id) {
        super("Could not find entity with id: " + id);
    }
    public NoSuchEntityException(String name) {
        super("Could not find user by name: " + name);
    }
}
