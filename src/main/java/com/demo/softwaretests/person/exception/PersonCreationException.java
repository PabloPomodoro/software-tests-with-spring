package com.demo.softwaretests.person.exception;

public class PersonCreationException extends RuntimeException{

    public PersonCreationException(String reason) {
        super(reason);
    }
}
