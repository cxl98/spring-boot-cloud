package com.cxl.elFinder.service.impl;

import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeSecurity;
import com.cxl.elFinder.core.impl.SecurityConstant;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.ThumbnailWidth;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class ElfinderStorageimpl implements ElfinderStorage {
    private static final String[][] ESCAPES = {{"+", "_P"}, {"-", "_M"}, {"/", "_S"}, {".", "_D"}, {"=", "_E"}};

    private List<Volume> volumes;
    private List<VolumeSecurity> volumeSecurities;
    private ThumbnailWidth thumbnailWidth;
    private Map<Volume, String> volumeIds;
    private Map<Volume, Locale> volumeLocales;


    @Override
    public Target fromHash(String hash) {
        for (Volume v : volumes) {
            String prefix = getVolumeId(v) + "_";
            if (prefix.equals(hash)) {
                return v.getRoot();
            }

            if (hash.startsWith(prefix)) {
                String localHash = hash.substring(prefix.length());

                for (String[] pair : ESCAPES) {
                    localHash = localHash.replace(pair[1], pair[0]);
                }

                String relativePath = new String(Base64.decodeBase64(localHash));
                return v.fromPath(relativePath);
            }
        }
        return null;
    }

    @Override
    public String getHash(Target target) throws IOException {
        String path = target.getVolume().getPath(target);
        String base = new String(Base64.encodeBase64(path.getBytes(StandardCharsets.UTF_8)));

        for (String[] pair : ESCAPES) {
            base = base.replace(pair[0], pair[1]);
        }
        return getVolumeId(target.getVolume()) + "_" + base;
    }

    @Override
    public String getVolumeId(Volume volume) {
        return volumeIds.get(volume);
    }

    @Override
    public Locale getVolumeLocale(Volume volume) {
        return volumeLocales.get(volume);
    }

    @Override
    public VolumeSecurity getVolumeSecurity(Target target) {

        try {
            final String hash = getHash(target);
            final String targetHashFirstChar = Character.toString(hash.charAt(0));
            final List<VolumeSecurity> volumeSecurities = getVolumeSecurities();

            for (VolumeSecurity volumeSecurity : volumeSecurities) {
                String pattern = volumeSecurity.getVolumePattern();

                //检查volume 模式是否和hash第一个字符相等.如果是的话,该模式没有正则表达式，将使用默认的模式。

                if (targetHashFirstChar.equalsIgnoreCase(pattern)) {
                    pattern = pattern + ElFinderConstansts.ELFINDER_SERCURITY_REGEX;
                }
                if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(hash).matches()) {
                    return volumeSecurity;
                }
            }
            //返回模式的安全模式
            return new VolumeSecurity() {
                @Override
                public String getVolumePattern() {
                    return targetHashFirstChar + ElFinderConstansts.ELFINDER_SERCURITY_REGEX;
                }

                @Override
                public SecurityConstant getSecurityConstraint() {
                    return new SecurityConstant();
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Unable to get target hash from elfinderStorage");
        }
    }

    @Override
    public List<Volume> getVolumes() {
        return volumes;
    }

    @Override
    public List<VolumeSecurity> getVolumeSecurities() {
        return volumeSecurities;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public void setVolumeSecurities(List<VolumeSecurity> volumeSecurities) {
        this.volumeSecurities = volumeSecurities;
    }

    public ThumbnailWidth getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(ThumbnailWidth thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public Map<Volume, String> getVolumeIds() {
        return volumeIds;
    }

    public void setVolumeIds(Map<Volume, String> volumeIds) {
        this.volumeIds = volumeIds;
    }

    public Map<Volume, Locale> getVolumeLocales() {
        return volumeLocales;
    }

    public void setVolumeLocales(Map<Volume, Locale> volumeLocales) {
        this.volumeLocales = volumeLocales;
    }
}
