package com.cxl.elFinder.support.archiver;

/**
 * Archiver支持的类型
 */
public enum ArchiveType {
    TAR {
        @Override
        public Archiver getStrategy() {
            return new TarArchive();
        }
    },
    ZIP {
        @Override
        public Archiver getStrategy() {
            return new ZipArchive();
        }
    },
    GZIP {
        @Override
        public Archiver getStrategy() {
            return new GzipArchive();
        }
    };

    public static ArchiveType metch(String mimeType) {
        for (ArchiveType archiveType : ArchiveType.values()) {
            if (mimeType.equalsIgnoreCase(archiveType.getStrategy().getMimeType())){
                return archiveType;
            }
        }
        throw new RuntimeException("不支持该类型");
    }
    public static String[] SUPPORT_MIME_TYPES={
            TAR.getMimeType(),
            ZIP.getMimeType(),
            GZIP.getMimeType()
    };
    public String getMimeType(){
        return getStrategy().getMimeType();
    }
    public String getExtension(){
        return getStrategy().getExtension();
    }
    public abstract Archiver getStrategy();
}
