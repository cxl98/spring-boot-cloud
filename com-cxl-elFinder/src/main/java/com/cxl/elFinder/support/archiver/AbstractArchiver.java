package com.cxl.elFinder.support.archiver;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 抽象Archiver定义了一些存档行为，这个类有一些
 * 通用的方法。这个类必须通过具体的存档来扩展
 * 实现
 */
public abstract class AbstractArchiver implements Archiver {
    /**
     * DEFAULT_ARCHIVE_NAME 默认文档名
     */
    private static final String DEFAULT_ARCHIVE_NAME = "Archive";

    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 定义一个怎么样创建文档的输入流
     *
     * @param bufferedInputStream the Inputstream.
     * @return 返回一个输入流
     * @throws IOException if something goes wrong.
     */
    public abstract ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException;

    /**
     * 定义一个怎么样创建文档的输出流
     *
     * @param bufferedOutputStream the Outputstream.
     * @return 返回一个输出流
     * @throws IOException if something goes wrong.
     */
    public abstract ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException;

    /**
     * 定义怎么创建一个文档的实体
     *
     * @param targetPath    the target path.
     * @param targetSize    the target size.
     * @param targetContent the target bytes.
     * @return the archive entry.
     */
    public abstract ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetContent);

    @Override
    public String getArchiveName() {
        return DEFAULT_ARCHIVE_NAME;
    }

    /**
     * .tar和.tgz使用的默认压缩实现
     *
     * @return 压缩后的存档目标
     */
    @Override
    public Target compress(Target... targets) throws IOException {
        Target compressTarget = null;

        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ArchiveOutputStream archiveOutputStream = null;
        try {
            for (Target target : targets) {
                // get target volume   获取目标卷
                final Volume targetVolume = target.getVolume();

                //获取目标的信息
                final String targetName = targetVolume.getName(target);
                final String targetDir = targetVolume.getParent(target).toString();
                final boolean targetisFolder = targetVolume.isFolder(target);
                if (compressTarget == null) {
                    //创建压缩文件
                    String compressFileName = (targets.length == 1) ? targetName : getArchiveName();
                    Path compressFile = Paths.get(targetDir, compressFileName + getExtension());
                    //创建一个新的压缩文件，如果已经存在则不重写
                    //如果您不希望出现这种行为，只需注释这一行
                    compressFile = createFile(true, compressFile.getFileName().getParent(), compressFile);

                    compressTarget = targetVolume.fromPath(compressFile.toString());
                    //打开流以写入压缩目标内容并自动关闭它
                    outputStream = targetVolume.openOutputStream(compressTarget);
                    bufferedOutputStream = new BufferedOutputStream(outputStream);
                    archiveOutputStream = createArchiveOutputStream(bufferedOutputStream);
                }
                if (targetisFolder) {
                    //压缩目标文件夹
                    compressDirectory(target, archiveOutputStream);
                } else {
                    //压缩文件
                    compressFile(target, archiveOutputStream);
                }

            }
        } finally {
            //关闭流
            if (archiveOutputStream != null) {
                archiveOutputStream.finish();
                archiveOutputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }

        }
        return compressTarget;
    }

    /**
     * .tar和.tgz使用的默认解压缩实现
     *
     * @param target the compress archive to decompress
     *               以压缩存档为目标进行解压缩
     * @return 返回解压目标文件
     */
    @Override
    public Target decompress(Target target) throws IOException {
        Target decompressTarget = null;
        final Volume volume = target.getVolume();

        //得到一个压缩目标信息
        final String src = target.toString();
        final String dest = removeExtension(src);


        //打开一个流以读取压缩目标内容并自动关闭它
        ArchiveInputStream archiveInputStream = null;
        OutputStream bufferedOutputStream = null;
        try {
            archiveInputStream= createArchiveInputStream(new BufferedInputStream(volume.openInputStream(target)));

            if (null != archiveInputStream) {
                //创建解压缩目标信息
                Path decompressDir = Paths.get(dest);

                //创建一个新的解压缩文件夹，如果已经存在，则不覆盖该文件夹,不存在就覆盖他
                decompressDir = createFile(false, decompressDir.getParent(), decompressDir);

                //创建解压缩目标信息
                decompressTarget = volume.fromPath(decompressDir.toString());

                //如果目标文件夹不存在，则创建目标文件夹
                volume.createFolder(decompressTarget);

                //获取压缩目标列表
                ArchiveEntry entry;
                while (null != (entry = archiveInputStream.getNextEntry())) {
                    if (archiveInputStream.canReadEntryData(entry)) {
                        //获取列表的信息
                        final String entryName = entry.getName();
                        final Target target1 = volume.fromPath(Paths.get(decompressDir.toString(), entryName).toString());
                        final Target parent = volume.getParent(target1);

                        //如果父文件夹不存在，则创建这个文件夹
                        if (null != parent && !volume.exists(parent)) {
                            volume.createFolder(parent);
                        }
                        if (!entry.isDirectory()) {
                            //打开一个流以读取压缩目标内容并自动关闭它
                             bufferedOutputStream = new BufferedOutputStream(volume.openOutputStream(target));
                            IOUtils.copy(archiveInputStream, bufferedOutputStream);
                        }
                    }
                }
            }
        } finally {
            if (null != archiveInputStream){
                archiveInputStream.close();
            }
            if (null!=bufferedOutputStream){
                bufferedOutputStream.close();
            }
        }
        return decompressTarget;
    }

    /**
     * 从给定的压缩文件名中删除扩展名。
     *
     * @param name 压缩源名称
     * @return 返回扩展名
     */
    static String removeExtension(String name) {
        if (name == null) {
            return null;
        } else {
            int index = name.lastIndexOf('.');
            return index == -1 ? name : name.substring(0, index);
        }
    }

    /**
     * 定义压缩目标文件夹的格式
     * @param target 压缩文件夹
     * @param archiveOutputStream  outputstream
     * @throws IOException 异常处理
     */
    protected void compressDirectory(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        Volume targetVolume = target.getVolume();
        Target[] targetChildrens = targetVolume.listChildren(target);

        for (Target targetChildren : targetChildrens) {
            if (targetVolume.isFolder(targetChildren)) {
                compressDirectory(targetChildren, archiveOutputStream);
            } else {
                compressFile(targetChildren, archiveOutputStream);
            }
        }
    }

    /**
     * 定义压缩目标文件的格式
     *
     * @param target              要在outpustream中写入的目标。
     * @param archiveOutputStream 存档输出流
     * @throws IOException 异常处理
     */
    final void compressFile(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        addTargetToArchiveOutputStream(target, archiveOutputStream);
    }

    /**
     * 增加一个目标到 outputstream流中
     *
     * @param target              要增加的目标
     * @param archiveOutputStream 存档输出流;
     * @throws IOException 异常处理
     */
    private void addTargetToArchiveOutputStream(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        Volume targetVolume = target.getVolume();

        InputStream targetInputStream = null;
        try {
            targetInputStream = targetVolume.openInputStream(target);
            final long targetSize = targetVolume.getSize(target);
            final byte[] targetContent = new byte[(int) targetSize];
            final String targetPath = targetVolume.getPath(target);
            ArchiveEntry entry = createArchiveEntry(targetPath, targetSize, targetContent);
            archiveOutputStream.putArchiveEntry(entry);
            IOUtils.copy(targetInputStream, archiveOutputStream);
            archiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (targetInputStream != null) {
                targetInputStream.close();
            }
        }
    }

    /**
     * 创建一个文件
     *
     * @param compressFile 是否是压缩文件
     * @param parent       父路径
     * @param path         自己的路径
     * @return
     */
    final Path createFile(boolean compressFile, Path parent, Path path) {
        Path archiveFile = path;
        if (Files.exists(archiveFile)) {
            String archiveName = getArchiveName() + count.getAndIncrement();
            if (compressFile) {
                archiveName += getExtension();
            }
            archiveFile = createFile(compressFile, parent, Paths.get(parent.toString(), archiveName));
        }
        return archiveFile;
    }
}
