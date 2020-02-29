package com.cxl.elFinder.param;

import lombok.Data;

/**
 * Constraint 限制，约束
 */
@Data
public class Constraint {
    private boolean locked;
    private boolean readable;
    private boolean writable;

}
