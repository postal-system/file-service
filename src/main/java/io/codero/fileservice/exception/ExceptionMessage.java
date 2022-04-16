package io.codero.fileservice.exception;

public enum ExceptionMessage {
    IOEXCEPTION("It was throw IOException, file cannot be deleted"),
    FILE_NOT_FOUND_EXCEPTION("This file cannot be found"),
    FILE_IS_EXIST("The file with name: %s already exists");
    private final String message;

    public String get() {
        return message;
    }

    private ExceptionMessage(String message) {
        this.message = message;
    }
}
