package com.cxl.elFinder.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ElFinderContext {

    HttpServletRequest getRequest();
    HttpServletResponse getResponse();
}
