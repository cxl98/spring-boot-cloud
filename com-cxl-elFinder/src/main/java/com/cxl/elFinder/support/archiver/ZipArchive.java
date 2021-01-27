package com.cxl.elFinder.support.archiver;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

public class ZipArchive extends AbstractArchiver implements Archiver {
    private static CRC32 crc32 = null;

    @Override
    public String getMimeType() {
        return "application/zip";
    }

    @Override
    public String getExtension() {
        return ".zip";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetContent) {
        ZipArchiveEntry entry = new ZipArchiveEntry(targetPath);
        entry.setSize(targetSize);
        entry.setMethod(ZipEntry.STORED);
        if (null != targetContent) {
            entry.setCrc(crc32Checksum(targetContent));
        }
        return entry;
    }

    private static long crc32Checksum(byte[] bytes) {
        crc32 = new CRC32();
        crc32.update(bytes);
        long checksum = crc32.getValue();
        crc32.reset();
        return checksum;
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        return new ZipArchiveInputStream(bufferedInputStream);
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        return new ZipArchiveOutputStream(bufferedOutputStream);
    }

    public ArchiveOutputStream createArchiveOutputStream(Path path) throws IOException {
        return new ZipArchiveOutputStream(path.toFile());
    }
    /**
     * .zip使用的解压缩实现
     *
     * @param target the compress archive to decompress
     *               以压缩存档为目标进行解压缩
     * @return 返回解压目标文件
     */
    @Override
    public Target decompress(Target target) throws IOException {
        Target decompressTarget;
        final Volume volume=target.getVolume();
        //得到一个压缩目标信息
        final String src=target.toString();
        String dest=removeExtension(src);

        //创建zipFile实例以读取压缩目标内容并自动关闭它
        try (ZipFile zipFile=new ZipFile(src)){
            //创建解压缩目标信息
            Path decompressDir= Paths.get(dest);
            //创建一个新的解压缩文件夹，如果已经存在，则不覆盖该文件夹,不存在就覆盖他
            decompressDir=createFile(false,decompressDir.getParent(),decompressDir);

            decompressTarget=volume.fromPath(decompressDir.toString());

            //如果目标文件夹不存在，则创建目标文件夹
            volume.createFolder(decompressTarget);

            //获取压缩目标列表
            Enumeration<ZipArchiveEntry> entries=zipFile.getEntries();

            while(entries.hasMoreElements()){
                final ZipArchiveEntry zipArchiveEntry=entries.nextElement();
                if (zipFile.canReadEntryData(zipArchiveEntry)) {
                    //获取实体的信息
                    final String entryName=zipArchiveEntry.getName();
                    final InputStream archiveInputStream=zipFile.getInputStream(zipArchiveEntry);
                    final Target target1=volume.fromPath(Paths.get(decompressDir.toString(),entryName).toString());

                    final Target parent=volume.getParent(target1);

                    //如果父文件夹不存在，则创建这个文件夹
                    if (null!=parent && !volume.exists(parent)){
                        volume.createFolder(parent);
                    }

                    if (!zipArchiveEntry.isDirectory()){
                        //打开一个流以读取压缩目标内容并自动关闭它
                        try (OutputStream outputStream=new BufferedOutputStream(volume.openOutputStream(target1))){
                            IOUtils.copy(archiveInputStream,outputStream);
                        }
                    }
                }
            }

        }
        return decompressTarget;
    }
}
