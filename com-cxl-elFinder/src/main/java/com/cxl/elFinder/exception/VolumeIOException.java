package com.cxl.elFinder.exception;

import java.io.IOException;

public class VolumeIOException extends IOException {
    private static final long serialVersionUID=107L;

    public VolumeIOException() {
        super();
    }

    public VolumeIOException(String message) {
        super(message);
    }
}
