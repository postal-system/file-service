package io.codero.fileservice.exception;

public enum ExceptionMessage {
    IOEXCEPTION("It was throw IOException, file cannot be deleted"),
    FILE_IS_EXIST("The file with name: %s already exists");
    private final String Message;

    public String get() {
        return Message;
    }

    private ExceptionMessage(String Message) {
        this.Message = Message;
    }
}
