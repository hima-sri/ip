package com.thbs.questionMS.exception;

public class QuestionNotFoundException extends Throwable{


    public QuestionNotFoundException(){
        super();
    }

    public  QuestionNotFoundException(String message){
        super(message);
    }
}
