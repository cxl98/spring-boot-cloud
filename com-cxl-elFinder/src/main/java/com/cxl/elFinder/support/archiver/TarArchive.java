package com.cxl.elFinder.support.archiver;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class TarArchive extends AbstractArchiver implements Archiver {
    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        return new TarArchiveInputStream(bufferedInputStream);
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        return new TarArchiveOutputStream(bufferedOutputStream);
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetContent) {
        TarArchiveEntry entry=new TarArchiveEntry(targetPath);
        entry.setSize(targetSize);
        return entry;
    }

    @Override
    public String getMimeType() {
        return "application/x-tar";
    }
    @Override
    public String getExtension() {
        return ".tar";
    }

}
