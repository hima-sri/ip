package com.quiz.assessment.EceptionHandling;

public class AssessmentAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AssessmentAlreadyExistsException() {
        super("Assessment already exists.");
    }
}

