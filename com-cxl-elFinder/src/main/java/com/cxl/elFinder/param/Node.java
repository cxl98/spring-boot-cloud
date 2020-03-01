package com.cxl.elFinder.param;

import lombok.Data;

@Data
public class Node {
    private String source;
    private String alias;
    private String path;
    private boolean _default;
    private String locale;
    private Constraint constraint;
}
