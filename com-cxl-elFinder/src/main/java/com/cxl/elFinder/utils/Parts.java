package com.cxl.elFinder.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Parts implements Serializable {
    private static final long serialVersionUID = 420L;


    private String chunkId;
    // number of parts
    private long numberOfParts;
    private long totalSize;

    private String fileName;

    Map<Long, Part> parts = new HashMap<>();

    public Parts(String chunkId, long numberOfParts, long totalSize, String fileName) {
        this.chunkId = chunkId;
        this.numberOfParts = numberOfParts;
        this.totalSize = totalSize;
        this.fileName = fileName;
    }

    public synchronized void addPart(long partIndex, Part part) {
        parts.put(partIndex, part);
    }

    public boolean isReady() {
        return numberOfParts == parts.size();
    }

    public InputStream openInpurStream() throws IOException {
        return new InputStream() {

            long partIndex = 0;
            Part part = parts.get(partIndex);
            InputStream is = part.getContent().openStream();

            @Override
            public int read() throws IOException {

                while (true) {
                    int c = is.read();
                    if (-1 != c) {
                        return c;
                    }
                    long index=numberOfParts-1;
                    if (partIndex == index){
                        is.close();
                        return -1;
                    }

                    part=parts.get(++partIndex);
                    is.close();
                    is=part.getContent().openStream();
                }
            }
        };
    }
    //检查分区
    public void checkParts() throws IOException {
        long totalLength=0;
        for (int i = 0; i <numberOfParts ; i++) {
            Part part=parts.get(i);
            totalLength+=part.getSize();
        }

        if (totalSize!=totalLength){
            throw new IOException(String.format("invalid file size: excepted %d, but is %d",totalSize,totalLength));
        }
    }

    public void remove(HttpServletRequest request){
        String key=String.format("chunk:%s,fileName:%s ",chunkId,fileName);
        request.getServletContext().removeAttribute(key);

    }

    public static synchronized  Parts create(HttpServletRequest request,String chunkId,String fileName,long total,long totalSize){
        String key = String.format("chunk:%s,fileName:%s", chunkId, fileName);
        Parts parts= (Parts) request.getServletContext().getAttribute(key);

        if (null==parts){
            parts=new Parts(chunkId,total,totalSize,
                    fileName);
            request.getServletContext().setAttribute(key,parts);
        }
        return parts;
    }


    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public long getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(long numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<Long, Part> getParts() {
        return parts;
    }

    public void setParts(Map<Long, Part> parts) {
        this.parts = parts;
    }
}
