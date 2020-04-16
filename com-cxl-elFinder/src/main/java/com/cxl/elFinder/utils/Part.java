package com.cxl.elFinder.utils;

import org.apache.commons.fileupload.FileItemStream;

public class Part {
    private long start;
    private long size;
    private FileItemStream content;

    public Part(long start, long size, FileItemStream content) {
        this.start = start;
        this.size = size;
        this.content = content;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileItemStream getContent() {
        return content;
    }

    public void setContent(FileItemStream content) {
        this.content = content;
    }
}
