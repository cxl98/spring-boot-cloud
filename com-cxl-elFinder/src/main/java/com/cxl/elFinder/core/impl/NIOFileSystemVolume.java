package com.cxl.elFinder.core.impl;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeBuilder;
import com.cxl.elFinder.support.content.detect.Detector;
import com.cxl.elFinder.support.content.detect.NioFileSystemDetector;
<<<<<<< HEAD
import com.cxl.elFinder.support.nio.NioFileSystem;
import com.cxl.elFinder.support.nio.NioFileSystem;
=======
import com.cxl.elFinder.support.nio.NioSystem;
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2


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
<<<<<<< HEAD
        NioFileSystem.createFile(fromTarget(target));
=======
        NioSystem.createFile(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public void createFolder(Target target) throws IOException {
<<<<<<< HEAD
        NioFileSystem.createFolder(fromTarget(target));
=======
        NioSystem.createFolder(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public void deleteFile(Target target) throws IOException {
<<<<<<< HEAD
        NioFileSystem.deleteFile(fromTarget(target));
=======
        NioSystem.deleteFile(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public void deleteFolder(Target target) throws IOException {
<<<<<<< HEAD
        NioFileSystem.deleteFolder(fromTarget(target));
=======
        NioSystem.deleteFolder(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public boolean exists(Target target) {
<<<<<<< HEAD
        return NioFileSystem.exists(fromTarget(target));
=======
        return NioSystem.exists(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
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
<<<<<<< HEAD
        return NioFileSystem.getLastModifiedTimeMillis(fromTarget(target));
=======
        return NioSystem.getLastModifiedTimeMillis(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
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
<<<<<<< HEAD
        return NioFileSystem.getName(fromTarget(target));
=======
        return NioSystem.getName(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public Target getParent(Target target) {
<<<<<<< HEAD
        Path path=NioFileSystem.getParent(fromTarget(target));
=======
        Path path=NioSystem.getParent(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
        return fromPath(path);
    }

    @Override
    public String getPath(Target target) throws IOException {
<<<<<<< HEAD
        return NioFileSystem.getRelativePath(getRootDir(),fromTarget(target));
=======
        return NioSystem.getRelativePath(getRootDir(),fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public Target getRoot() {
        return fromPath(getRootDir());
    }

    @Override
    public long getSize(Target target) throws IOException {
        Path path=fromTarget(target);
<<<<<<< HEAD
        boolean recursiveSize=NioFileSystem.isFolder(path);
        return NioFileSystem.getTotalSizeInBytes(path,recursiveSize);
=======
        boolean recursiveSize=NioSystem.isFolder(path);
        return NioSystem.getTotalSizeInBytes(path,recursiveSize);
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public boolean hasChildFolder(Target target) throws IOException {
<<<<<<< HEAD
        return NioFileSystem.hasChildrenFolder(fromTarget(target));
=======
        return NioSystem.hasChildrenFolder(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public boolean isFolder(Target target) {
<<<<<<< HEAD
        return NioFileSystem.isFolder(fromTarget(target));
=======
        return NioSystem.isFolder(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }



    @Override
    public boolean isRoot(Target target) throws IOException {
<<<<<<< HEAD
        return NioFileSystem.isSame(getRootDir(),fromTarget(target));
=======
        return NioSystem.isSame(getRootDir(),fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public Target[] listChildren(Target target) throws IOException {
<<<<<<< HEAD
        List<Path> childrenResultList=NioFileSystem.listChildrenNotHidden(fromTarget(target));
=======
        List<Path> childrenResultList=NioSystem.listChildrenNotHidden(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
        List<Target> targets=new ArrayList<>(childrenResultList.size());
        for (Path path: childrenResultList) {
            targets.add(fromPath(path));
        }
        return targets.toArray(new Target[targets.size()]);
    }

    @Override
    public InputStream openInputStream(Target target) throws IOException {
<<<<<<< HEAD
        return NioFileSystem.openInputStream(fromTarget(target));
=======
        return NioSystem.openInputStream(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public OutputStream openOutputStream(Target target) throws IOException {
<<<<<<< HEAD
        return NioFileSystem.openOutputStream(fromTarget(target));
=======
        return NioSystem.openOutputStream(fromTarget(target));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public void rename(Target origin, Target destination) throws IOException {
<<<<<<< HEAD
        NioFileSystem.rename(fromTarget(origin),fromTarget(destination));
=======
        NioSystem.rename(fromTarget(origin),fromTarget(destination));
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
    }

    @Override
    public List<Target> search(String target) throws IOException {
<<<<<<< HEAD
        List<Path> searchResultList=NioFileSystem.search(getRootDir(),target);
=======
        List<Path> searchResultList=NioSystem.search(getRootDir(),target);
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
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
