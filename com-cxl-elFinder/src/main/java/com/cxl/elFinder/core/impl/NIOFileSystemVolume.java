package com.cxl.elFinder.core.impl;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeBuilder;
import com.cxl.elFinder.support.content.detect.Detector;
import com.cxl.elFinder.support.content.detect.NioFileSystemDetector;
import com.cxl.elFinder.support.nio.NioSystem;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NIOFileSystemVolume implements Volume {
    private final String alias;
    private final Path rootDir;
    private final Detector detector;

    public NIOFileSystemVolume(Builder builder) {
        this.alias = builder.alias;
        this.rootDir = builder.rootDir;
        this.detector = new NioFileSystemDetector();
    }

    private void createRootDir() {
        try {
            Target target = fromPath(rootDir);
            if (!exists(target)) {
                createFolder(target);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create root dir folder", e);
        }
    }

    private static Path fromTarget(Target target) {
        return ((NIOFileSystemTarget) target).getPath();
    }

    private Target fromPath(Path path) {
        return fromPath(this, path);
    }

    private static Target fromPath(NIOFileSystemVolume volume, Path path) {
        return new NIOFileSystemTarget(volume, path);
    }

    public Path getRootDir() {
        return rootDir;
    }

    @Override
    public void createFile(Target target) throws IOException {
        NioSystem.createFile(fromTarget(target));
    }

    @Override
    public void createFolder(Target target) throws IOException {
        NioSystem.createFolder(fromTarget(target));
    }

    @Override
    public void deleteFile(Target target) throws IOException {
        NioSystem.deleteFile(fromTarget(target));
    }

    @Override
    public void deleteFolder(Target target) throws IOException {
        NioSystem.deleteFolder(fromTarget(target));
    }

    @Override
    public boolean exists(Target target) {
        return NioSystem.exists(fromTarget(target));
    }

    @Override
    public Target fromPath(String relativePath) {
        String rootDir=getRootDir().toString();
        Path path;
        if (relativePath.startsWith(rootDir)) {
            path= Paths.get(relativePath);
        }else{
            path=Paths.get(rootDir,relativePath);
        }

        return fromPath(path);
    }

    @Override
    public long getLastModified(Target target) throws IOException {
        return NioSystem.getLastModifiedTimeMillis(fromTarget(target));
    }

    @Override
    public String getMimeType(Target target) throws IOException {
        Path path=fromTarget(target);
        return detector.detect(path);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getName(Target target) {
        return NioSystem.getName(fromTarget(target));
    }

    @Override
    public Target getParent(Target target) {
        Path path=NioSystem.getParent(fromTarget(target));
        return fromPath(path);
    }

    @Override
    public String getPath(Target target) throws IOException {
        return NioSystem.getRelativePath(getRootDir(),fromTarget(target));
    }

    @Override
    public Target getRoot() {
        return fromPath(getRootDir());
    }

    @Override
    public long getSize(Target target) throws IOException {
        Path path=fromTarget(target);
        boolean recursiveSize=NioSystem.isFolder(path);
        return NioSystem.getTotalSizeInBytes(path,recursiveSize);
    }

    @Override
    public boolean hasChildFolder(Target target) throws IOException {
        return NioSystem.hasChildrenFolder(fromTarget(target));
    }

    @Override
    public boolean isFolder(Target target) {
        return NioSystem.isFolder(fromTarget(target));
    }



    @Override
    public boolean isRoot(Target target) throws IOException {
        return NioSystem.isSame(getRootDir(),fromTarget(target));
    }

    @Override
    public Target[] listChildren(Target target) throws IOException {
        List<Path> childrenResultList=NioSystem.listChildrenNotHidden(fromTarget(target));
        List<Target> targets=new ArrayList<>(childrenResultList.size());
        for (Path path: childrenResultList) {
            targets.add(fromPath(path));
        }
        return targets.toArray(new Target[targets.size()]);
    }

    @Override
    public InputStream openInputStream(Target target) throws IOException {
        return NioSystem.openInputStream(fromTarget(target));
    }

    @Override
    public OutputStream openOutputStream(Target target) throws IOException {
        return NioSystem.openOutputStream(fromTarget(target));
    }

    @Override
    public void rename(Target origin, Target destination) throws IOException {
        NioSystem.rename(fromTarget(origin),fromTarget(destination));
    }

    @Override
    public List<Target> search(String target) throws IOException {
        List<Path> searchResultList=NioSystem.search(getRootDir(),target);
        List<Target> targets=new ArrayList<>(searchResultList.size());
        for (Path path: searchResultList) {
            targets.add(fromPath(path));
        }
        return Collections.unmodifiableList(targets);
    }
    public static Builder builder(String alias,Path rootDir){
        return new NIOFileSystemVolume.Builder(alias, rootDir);
    }
    private static class Builder implements VolumeBuilder<NIOFileSystemVolume> {
        private final String alias;
        private final Path rootDir;

        public Builder(String alias, Path rootDir) {
            this.alias = alias;
            this.rootDir = rootDir;
        }

        @Override
        public NIOFileSystemVolume build() {
            return new NIOFileSystemVolume(this);
        }
    }

}
