package com.cxl.elFinder.support.archiver;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class GzipArchive extends AbstractArchiver implements Archiver {


    @Override
    public String getMimeType() {
        return "application/x-gzip";
    }

    @Override
    public String getExtension() {
        return ".tgz";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetContent) {
        TarArchiveEntry entry=new TarArchiveEntry(targetPath);
        entry.setSize(targetSize);
        return entry;
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        return new TarArchiveOutputStream(new GzipCompressorOutputStream(bufferedOutputStream));
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        return new TarArchiveInputStream(new GzipCompressorInputStream(bufferedInputStream));
    }
}
