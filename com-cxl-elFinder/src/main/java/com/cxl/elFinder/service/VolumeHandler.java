package com.cxl.elFinder.service;

import com.cxl.elFinder.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeSecurity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Volume 的处理器　主要有创建文件，删除文件等操作
 */
public class VolumeHandler implements Serializable {
    private static final long serialVersionUID = 8855615616525021L;

    private final Volume volume;

    private final Target target;
    private final VolumeSecurity volumeSecurity;
    private final ElfinderStorage elfinderStorage;

    public VolumeHandler(Target target, ElfinderStorage elfinderStorage) {
        this.target = target;
        this.volume = target.getVolume();
        this.volumeSecurity = elfinderStorage.getVolumeSecurity(target);
        this.elfinderStorage = elfinderStorage;
    }

    public VolumeHandler(VolumeHandler parent, String name) throws IOException {
        this.volume = parent.volume;
        this.elfinderStorage = parent.elfinderStorage;
        this.target = volume.fromPath(volume.getPath(parent.target) + ElFinderConstansts.ELFINDER_PARAMETER_FILE_SEPARATOR + name);
        this.volumeSecurity = elfinderStorage.getVolumeSecurity(target);
    }

    public void createFile() throws IOException {
        volume.createFile(target);
    }

    public void createFolder() throws IOException {
        volume.createFolder(target);
    }

    public void delete() throws IOException {
        if (volume.isFolder(target)) {
            volume.deleteFolder(target);
        } else {
            volume.deleteFile(target);
        }
    }

    public boolean exists() {
        return volume.exists(target);
    }

    public long getLastModified() throws IOException {
        return volume.getLastModified(target);
    }
    public String getMimeType() throws IOException {
        return volume.getMimeType(target);
    }
    public String getName() {
        return volume.getName(target);
    }

    public boolean hasChildFolder() throws IOException {
        return volume.hasChildFolder(target);
    }

    public boolean isFolder() {
        return volume.isFolder(target);
    }

    public long getSize() throws IOException {
        return volume.getSize(target);
    }

    public boolean isRoot() throws IOException {
        return volume.isRoot(target);
    }

    public List<VolumeHandler> listChildren() throws IOException {
        List<VolumeHandler> list = new ArrayList<>();
        for (Target target : volume.listChildren(target)) {
            list.add(new VolumeHandler(target, elfinderStorage));
        }
        return list;
    }

    public InputStream openInputStream() throws IOException {
        return volume.openInputStream(target);
    }

    public OutputStream openOutputStream() throws IOException {
        return volume.openOutputStream(target);
    }

    public VolumeHandler getParent() {
        return new VolumeHandler(volume.getParent(target), elfinderStorage);
    }

    public String getVloumeId() {
        return elfinderStorage.getVolumeId(volume);
    }

    public String getHash() throws IOException {
        return elfinderStorage.getHash(target);
    }

    public boolean isLocked() {
        return this.volumeSecurity.getSecurityConstraint().isLocked();
    }

    public boolean isReadable() {
        return this.volumeSecurity.getSecurityConstraint().isReadable();
    }
    public boolean isWritable() {
        return this.volumeSecurity.getSecurityConstraint().isWritable();
    }

    public void renameTo(VolumeHandler dst) throws IOException {
        volume.rename(target, dst.target);
    }

    public String getVolumeAlias() {
        return volume.getAlias();
    }

    public Volume getVolume() {
        return volume;
    }

    public Target getTarget() {
        return target;
    }

    public VolumeSecurity getVolumeSecurity() {
        return volumeSecurity;
    }

    public ElfinderStorage getElfinderStorage() {
        return elfinderStorage;
    }
}
