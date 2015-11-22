package com.shopchat.consumer.models;

public class ErrorModel {

    private Error errorType;
    private String errorMessage;

    public enum Error {
        ERROR_TYPE_NO_NETWORK, ERROR_TYPE_SERVER, ERROR_TYPE_BAD_REQUEST, ERROR_TYPE_UNAUTHORIZED, ERROR_TYPE_CONFLICT
    }

    public Error getErrorType() {
        return errorType;
    }

    public void setErrorType(Error errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
