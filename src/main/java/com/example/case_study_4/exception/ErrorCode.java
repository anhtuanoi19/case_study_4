package com.example.case_study_4.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "error.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "error.invalidInput", HttpStatus.BAD_REQUEST),
    LIST_STUDENT_NOT_FOUND(1002, "error.listStudentNotFound", HttpStatus.NOT_FOUND),
    STUDENT_NOT_FOUND(1003, "error.studentNotFound", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(1004, "error.courseNotFound", HttpStatus.NOT_FOUND),
    LIST_COURSE_NOT_FOUND(1005, "error.listCourseNotFound", HttpStatus.NOT_FOUND),
    STUDENT_EMAIL_EXISTS(1006, "error.studentEmailExists", HttpStatus.BAD_REQUEST),
    STUDENT_DISCONECT(1007, "error.studentDisconnect", HttpStatus.BAD_REQUEST),
    ALREADY_DELETED(1008, "error.alreadyDeleted", HttpStatus.BAD_REQUEST),
    STUDENT_EXISTS_COURSE(1009, "error.existsCourse", HttpStatus.BAD_REQUEST),
    COURSE_CLOSE(1010, "error.courseClose", HttpStatus.NOT_FOUND),
    INVALID_NAME(1011, "error.invalid", HttpStatus.NOT_FOUND),
    NAME_TOO_LONG(1012, "error.nameTooLong", HttpStatus.NOT_FOUND);


    private final int code;
    private final String messageKey;
    private final HttpStatus statusCode;

    ErrorCode(int code, String messageKey, HttpStatus statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
