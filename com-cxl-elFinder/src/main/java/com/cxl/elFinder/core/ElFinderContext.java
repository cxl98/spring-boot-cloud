package com.cxl.elFinder.core;

import com.cxl.elFinder.service.ElfinderStorageFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ElFinderContext {
    ElfinderStorageFactory getVolumeSourceFactory();
    HttpServletRequest getRequest();
    HttpServletResponse getResponse();
}
