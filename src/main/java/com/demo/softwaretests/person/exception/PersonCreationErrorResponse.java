package com.demo.softwaretests.person.exception;

public class PersonCreationErrorResponse {

    private String errorMessage;
    private String exceptionReason;

    public PersonCreationErrorResponse(String errorMessage, String exceptionReason) {
        this.errorMessage = errorMessage;
        this.exceptionReason = exceptionReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }
}
