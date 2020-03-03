package com.cxl.elFinder.core.impl;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;

import java.nio.file.Path;

public class NIOFileSystemTarget implements Target {
    private final Path path;
    private final Volume volume;

    public NIOFileSystemTarget(Volume volume,Path path) {
        this.path = path;
        this.volume = volume;
    }

    @Override
    public Volume getVolume() {
        return volume;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path.toAbsolutePath().toString();
    }
}
