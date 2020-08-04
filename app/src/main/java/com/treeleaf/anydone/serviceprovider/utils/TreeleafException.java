package com.treeleaf.anydone.serviceprovider.utils;

public class TreeleafException extends Exception {
    public TreeleafException(String msg) {
        super(msg);
    }

    public TreeleafException(Throwable throwable) {
        super(throwable);
    }

    public TreeleafException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
