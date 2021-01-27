package com.cxl.elFinder.exception;

public class ElFinderException extends  RuntimeException{
    private static final long serialVersionUID=208L;

    public ElFinderException() {
        super();
    }

    public ElFinderException(String message) {
        super(message);
    }
}
