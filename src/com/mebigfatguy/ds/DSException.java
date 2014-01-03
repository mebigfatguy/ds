package com.mebigfatguy.ds;

public class DSException extends Exception {

    private static final long serialVersionUID = -428342381365817600L;

    public DSException(String message) {
        super(message);
    }
    
    public DSException(String message, Throwable cause) {
        super(message, cause);
    }
}
