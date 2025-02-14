package com.demo.softwaretests.exception;

public class PersonCreationException extends RuntimeException{

    public PersonCreationException(String reason) {
        super(reason);
    }
}
