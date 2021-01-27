package com.cxl.elFinder.utils;

import com.cxl.elFinder.service.VolumeHandler;

import java.io.IOException;
import java.io.InputStream;

public interface FileWriter {
    VolumeHandler createAndSave(String fileName, InputStream inputStream) throws IOException;
}
