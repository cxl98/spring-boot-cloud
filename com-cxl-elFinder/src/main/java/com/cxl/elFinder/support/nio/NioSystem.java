package com.cxl.elFinder.support.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NioSystem {
    public NioSystem() {
        throw new AssertionError();
    }

    public static void createFile(Path path) throws IOException {
        Files.createFile(path);
    }

    public static void createFolder(Path path) throws IOException {
        if (!isFolder(path)) {
            Files.createDirectories(path);
        }
    }

    public static void deleteFile(Path path) throws IOException {
        if (!isFolder(path)) {
            boolean b = Files.deleteIfExists(path);
            System.out.println("delete--------"+b);
        }
    }
    public static boolean isFolder(Path path) {
        return Files.isDirectory(path);
    }

    public static void deleteFolder(Path path) throws IOException {
        deleteFolder(path, true);
    }

    private static void deleteFolder(Path path, boolean b) throws IOException {
        if (isFolder(path)) {
            if (b) {
                List<Path> listChildren = listChildren(path);
                for (Path item : listChildren) {
                    if (isFolder(item)) {
                        deleteFolder(item, b);
                    } else {
                        deleteFolder(item);
                    }
                }
            }
            Files.deleteIfExists(path);
        }
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static long getLastModifiedTimeMillis(Path path) throws IOException {
        return Files.getLastModifiedTime(path).toMillis();
    }

    public static String getMineType(Path path) throws IOException {
        return Files.probeContentType(path);
    }

    public static String getName(Path path) {
        return path.getFileName().toString();
    }

    public static Path getParent(Path path) {
        return path.getParent();
    }

    /**
     * 基于根路径和目标路径获取相对路径。
     *
     * @param rootPath 根路径
     * @param path     目标路径
     * @return 相对路径
     * @throws IOException
     */
    public static String getRelativePath(Path rootPath, Path path) throws IOException {
        String relativePath = "";
        String root = rootPath.toString().trim();
        String p = path.toString().trim();
        if (!p.equalsIgnoreCase(root) && p.startsWith(root)) {
            relativePath = path.subpath(rootPath.getNameCount(), path.getNameCount()).toString();
        }
        return relativePath;
    }

    public static long getTotalSizeInBytes(Path path, boolean recursive) throws IOException {
        if (isFolder(path) && recursive) {
            FileTreeSize fileTreeSize = new FileTreeSize();
            Files.walkFileTree(path, fileTreeSize);
            return fileTreeSize.getTotalSize();
        }
        return Files.size(path);
    }

    public static boolean isSame(Path expected, Path actual) throws IOException {
        return Files.isSameFile(expected, actual);
    }

    public static boolean hasChildrenFolder(Path dir) throws IOException {
        if (isFolder(dir)) {
            DirectoryStream.Filter<Path> filter = entry -> Files.isDirectory(entry);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir, filter);
            return directoryStream.iterator().hasNext();
        }
        return false;
    }

    public static InputStream openInputStream(Path path) throws IOException {
        return Files.newInputStream(path);
    }

    public static OutputStream openOutputStream(Path path) throws IOException {
        return Files.newOutputStream(path);
    }

    public static void rename(Path origin, Path destination) throws IOException {
        Files.move(origin, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public static List<Path> search(Path path, String target, FileTreeSearch.MatchMode mode, boolean ignoreCase) throws IOException {
        if (isFolder(path)) {
            FileTreeSearch fileTreeSearch = new FileTreeSearch(target, mode, ignoreCase);
            Files.walkFileTree(path, fileTreeSearch);

            List<Path> paths = fileTreeSearch.getFoundPaths();
            return Collections.unmodifiableList(paths);
        }
        throw new IllegalArgumentException("The provided path is not a directory");
    }

    public static List<Path> search(Path path, String target) throws IOException {
        return search(path, target, FileTreeSearch.MatchMode.ANYWHERE, true);
    }

    private static List<Path> listChildren(Path path) throws IOException {
        return listChildren(path, null);
    }

    public static List<Path> listChildrenNotHidden(Path dir) throws IOException {
        DirectoryStream.Filter<Path> notHiddenFilter = path -> !Files.isHidden(path);
        return listChildren(dir, notHiddenFilter);
    }

    private static List<Path> listChildren(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException {
        if (isFolder(path)) {
            List<Path> list = new ArrayList<>();
            DirectoryStream<Path> directoryStream = (filter != null ? Files.newDirectoryStream(path, filter) : Files.newDirectoryStream(path));
            for (Path item : directoryStream) {
                list.add(item);
            }
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

    private static class FileTreeSize extends SimpleFileVisitor<Path> {
        private long totalSize;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            totalSize += attrs.size();
            return FileVisitResult.CONTINUE;
        }

        public long getTotalSize() {
            return totalSize;
        }
    }

    private static class FileTreeSearch extends SimpleFileVisitor<Path> {
        enum MatchMode {
            EXACT, ANYWHERE
        }

        private final String query;
        private final MatchMode mode;
        private final boolean ignoreCase;
        private final List<Path> foundPaths;

        public FileTreeSearch(String query, MatchMode mode, boolean ignoreCase) {
            this.query = query;
            this.mode = mode;
            this.ignoreCase = ignoreCase;
            this.foundPaths = new ArrayList<>();
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Objects.requireNonNull(dir);
            if (exc == null) {
                search(dir);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            super.visitFile(file, attrs);
            search(file);
            return FileVisitResult.CONTINUE;
        }

        private void search(Path path) {
            if (path != null && path.getFileName() != null) {
                String fileName = path.getFileName().toString();
                boolean found;
                switch (mode) {
                    case EXACT:
                        if (ignoreCase) {
                            found = fileName.equalsIgnoreCase(query);
                        } else {
                            found = fileName.equals(query);
                        }
                        break;
                    case ANYWHERE:
                        if (ignoreCase) {
                            found = fileName.toLowerCase().contains(query.toLowerCase());
                        } else {
                            found = fileName.contains(query);
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
                if (found) {
                    foundPaths.add(path);
                }
            }
        }

        public List<Path> getFoundPaths() {
            return Collections.unmodifiableList(foundPaths);
        }
    }
}
