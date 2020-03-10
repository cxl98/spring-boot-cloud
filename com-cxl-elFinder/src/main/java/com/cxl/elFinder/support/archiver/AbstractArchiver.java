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

import static org.apache.commons.io.FilenameUtils.removeExtension;

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
     * @throws IOException  if something goes wrong.
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
     *.tar和.tgz使用的默认压缩实现
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

                    compressTarget=targetVolume.fromPath(compressFile.toString());
                    //打开流以写入压缩目标内容并自动关闭它
                    outputStream=targetVolume.openOutputStream(compressTarget);
                    bufferedOutputStream=new BufferedOutputStream(outputStream);
                    archiveOutputStream=createArchiveOutputStream(bufferedOutputStream);
                }
                if (targetisFolder){
                    //压缩目标文件夹
                    compressDirectory(target,archiveOutputStream);
                }else{
                    //压缩文件
                    compressFile(target,archiveOutputStream);
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
     *.tar和.tgz使用的默认解压缩实现
     * @param target the compress archive to decompress
     * 以压缩存档为目标进行解压缩
     * @return 返回解压目标文件
     */
    @Override
    public Target decompress(Target target) throws IOException {
        Target decompressTarget;
        final Volume volume=target.getVolume();

        //得到一个压缩目标信息
        final String src=target.toString();
        final String dest=removeExtension(src)
        return null;
    }

    protected  void compressDirectory(Target target, ArchiveOutputStream archiveOutputStream) throws IOException{
        Volume targetVolume=target.getVolume();
        Target[] targetChildrens=targetVolume.listChildren(target);

        for (Target targetChildren: targetChildrens) {
            if (targetVolume.isFolder(targetChildren)) {
                compressDirectory(targetChildren,archiveOutputStream);
            }else{
                compressFile(targetChildren,archiveOutputStream);
            }
        }
    }

     final void compressFile(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        addTargetToArchiveOutputStream(target,archiveOutputStream);
     }

    private void addTargetToArchiveOutputStream(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        Volume targetVolume=target.getVolume();

        InputStream targetInputStream= null;
        try {
            targetInputStream = targetVolume.openInputStream(target);
            final long targetSize=targetVolume.getSize(target);
            final byte[] targetContent=new byte[(int) targetSize];
            final String targetPath=targetVolume.getPath(target);
            ArchiveEntry entry=createArchiveEntry(targetPath,targetSize,targetContent);
            archiveOutputStream.putArchiveEntry(entry);
            IOUtils.copy(targetInputStream,archiveOutputStream);
            archiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (targetInputStream != null) {
                targetInputStream.close();
            }
        }



    }

    protected final Path createFile(boolean compressFile, Path parent, Path path) {
        Path archiveFile=path;
        if (Files.exists(archiveFile)) {
            String archiveName=getArchiveName()+count.getAndIncrement();
            if (compressFile){
                archiveName+=getExtension();
            }
            archiveFile=createFile(compressFile,parent,Paths.get(parent.toString(),archiveName));
        }
        return archiveFile;
    }
}
