package com.admish.blog.exception;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException(String message){
        super(message);
    }
}
