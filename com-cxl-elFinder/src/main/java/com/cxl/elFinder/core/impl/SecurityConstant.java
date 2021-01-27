package com.cxl.elFinder.core.impl;

import lombok.Data;

@Data
public class SecurityConstant {
    private boolean locked=false;
    private boolean readable=true;
    private boolean writable=true;

}
