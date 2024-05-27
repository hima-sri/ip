package com.thbs.questionMS.exception;

public class ExcelProcessingException extends Throwable {
    public ExcelProcessingException(String message) {
        super(message);
    }

    public ExcelProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

