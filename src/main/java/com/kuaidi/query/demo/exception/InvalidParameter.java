package com.kuaidi.query.demo.exception;


public class InvalidParameter extends VcbException {

    private static final long serialVersionUID = -4530275043528852631L;

    public InvalidParameter(String field){
        super(400,"InvalidParameter", String.format("The specified parameter '%s' is not valid",field));
    }

    public InvalidParameter(String field, String message){
        super(400,"InvalidParameter", String.format("The specified parameter '%s' %s",field, message));
    }

}
