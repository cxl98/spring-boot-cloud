package com.cxl.elFinder.core.impl;

import com.cxl.elFinder.core.VolumeSecurity;

public class DefaultVolumeSecurity implements VolumeSecurity {
    private final String volumePattern;
    private final SecurityConstant securityConstant;

    public DefaultVolumeSecurity(String volumePattern, SecurityConstant securityConstant) {
        this.volumePattern = volumePattern;
        this.securityConstant = securityConstant;
    }

    @Override
    public String getVolumePattern() {
        return volumePattern;
    }

    @Override
    public SecurityConstant getSecurityConstraint() {
        return securityConstant;
    }
}
