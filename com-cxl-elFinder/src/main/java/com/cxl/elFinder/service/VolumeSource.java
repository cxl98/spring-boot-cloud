package com.cxl.elFinder.service;

import com.cxl.elFinder.utils.ElFinderConfigurationUtils;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeBuilder;
import com.cxl.elFinder.core.impl.NIOFileSystemVolume;
import com.cxl.elFinder.exception.VolumeSourceException;

import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;

public enum VolumeSource {
    FILESYSTEM {
        @Override
        public VolumeBuilder<?> getVolumeBuilder(String alias, String path) {
            return NIOFileSystemVolume.builder(alias, Paths.get(ElFinderConfigurationUtils.toURI(path)));
        }
    };

    public static VolumeSource source(String source) {
        if (null == source) {
            throw new VolumeSourceException(" Volume source not supported! The supported volumes sources are: " + Arrays.deepToString(values()).toLowerCase());
        } else {

            final String notLetterRegex = "[^\\p{L}]";
            final String whitespaceRegex = "[\\p{Z}]";
            final String notAsciiCharactersRegex = "[^\\p{ASCII}]";
            final String emptyString = "";
            source = Normalizer.normalize(source, Normalizer.Form.NFD);
            source = source.replaceAll(notLetterRegex, emptyString);
            source = source.replaceAll(whitespaceRegex, emptyString);
            source = source.replaceAll(notAsciiCharactersRegex, emptyString);
            source = source.toUpperCase();

            for (VolumeSource volumeSource : values()) {
                if (source.equalsIgnoreCase(volumeSource.name())) {
                    return volumeSource;
                }
                throw new VolumeSourceException("Volume source not supported ! The supported volumes source are；　" + Arrays.deepToString(values()).toLowerCase());
            }
        }
        throw new VolumeSourceException("Volume source not informed in elfinder configuration xml!");
    }

    public Volume newInstance(String alias, String path) {
        if (null == path || path.isEmpty()) {
            throw new VolumeSourceException("Volume source path not informed");
        }
        return getVolumeBuilder(alias, path).build();
    }

    public abstract VolumeBuilder<?> getVolumeBuilder(String alias, String path);
}
