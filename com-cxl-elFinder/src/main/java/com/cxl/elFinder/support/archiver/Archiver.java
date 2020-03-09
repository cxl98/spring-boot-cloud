package com.cxl.elFinder.support.archiver;

import com.cxl.elFinder.core.Target;

import java.io.IOException;

public interface Archiver {
    /**
     * 得到存档
     *
     * @return
     */
    String getArchiveName();

    /**
     * 媒体类型（通常称为 Multipurpose Internet Mail Extensions 或 MIME 类型 ）是一种标准，用来表示文档、文件或字节流的性质和格式。它在IETF RFC 6838中进行了定义和标准化。
     */
    String getMimeType();

    /**
     * 获取存档扩展名
     */
    String getExtension();

    /**
     *为给定目标创建压缩存档
     * @param targets
     * @return
     * @throws IOException
     */
    Target compress(Target...targets)throws IOException;

    /**
     * 为给定目标创建压缩存档
     *
     * @param target the compress archive to decompress
     * 以压缩存档为目标进行解压缩
     * @return the target folder of the decompressed targets.
     * 解压缩目标的目标文件夹
     */
    Target decompress(Target target) throws IOException;
}
