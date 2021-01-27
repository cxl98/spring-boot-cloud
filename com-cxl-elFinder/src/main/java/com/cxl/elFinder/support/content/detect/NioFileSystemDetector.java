package com.cxl.elFinder.support.content.detect;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

public class NioFileSystemDetector extends FileTypeDetector implements Detector {
    private final Tika tika=new Tika();


    @Override
    public String detect(InputStream inputStream) throws IOException {
        return tika.detect(inputStream);
    }

    @Override
    public String detect(Path path) throws IOException {
        if (Files.isDirectory(path)){
            return "directory";
        }
        return tika.detect(path);
    }

    @Override
    public String probeContentType(Path path) throws IOException {
        return detect(path);
    }
}
