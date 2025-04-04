package com.demo.softwaretests.person.exception;

public class PersonCreationErrorResponse {

    private final String errorMessage;
    private final String exceptionReason;

    public PersonCreationErrorResponse(String errorMessage, String exceptionReason) {
        this.errorMessage = errorMessage;
        this.exceptionReason = exceptionReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

}
