package com.thbs.questionMS.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class QuestionExceptionHandler {

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> questionNotFoundException(QuestionNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<String> subjectNotFoundException(SubjectNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<String> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file format. Please upload an Excel file.");
    }

    @ExceptionHandler(ExcelProcessingException.class)
    public ResponseEntity<String> handleExcelProcessingException(ExcelProcessingException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception while processing the Excel Sheet Data.");
    }

    @ExceptionHandler(UnableToGenerateQuestionPaperException.class)
    public ResponseEntity<String> handleUnableToGenerateQuestionPaperException(UnableToGenerateQuestionPaperException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(QuestionPaperNotFoundException.class)
    public ResponseEntity<String> handleQuestionPaperNotFoundException(QuestionPaperNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}

//    @ExceptionHandler(QuestionNotFoundException.class)
//    public ResponseEntity<String> questionNotFoundException(QuestionNotFoundException ex){
//        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(SubjectNotFoundException.class)
//    public ResponseEntity<String> subjectNotFoundException(SubjectNotFoundException ex){
//        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidFileFormatException.class)
//    public ResponseEntity<String> handleInvalidFileFormatException(InvalidFileFormatException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file format. Please upload an Excel file.");
//    }
//    @ExceptionHandler(ExcelProcessingException.class)
//    public ResponseEntity<String> handleExcelProcessingException(ExcelProcessingException ex){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception while processing the Excel Sheet Data.");
//    }
//    @ExceptionHandler(UnableToGenerateQuestionPaperException.class)
//    public ResponseEntity<String> handleUnableToGenerateQuestionPaperException(UnableToGenerateQuestionPaperException ex){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//    }
//    @ExceptionHandler(QuestionPaperNotFoundException.class)
//    public ResponseEntity<String> questionPaperNotFoundException(QuestionPaperNotFoundException ex){
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(QuestionPaperNotFoundException.class)
//    public ResponseEntity<String> handleQuestionPaperNotFoundException(QuestionPaperNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//





