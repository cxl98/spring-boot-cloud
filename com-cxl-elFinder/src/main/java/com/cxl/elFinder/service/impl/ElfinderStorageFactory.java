package com.cxl.elFinder.service.impl;

import com.cxl.elFinder.service.ElfinderStorage;

public class ElfinderStorageFactory implements com.cxl.elFinder.service.ElfinderStorageFactory {
    private ElfinderStorage elfinderStorage;
    @Override
    public ElfinderStorage getVolumeSource() {
        return elfinderStorage;
    }

    public void setElfinderStorage(ElfinderStorage elfinderStorage) {
        this.elfinderStorage = elfinderStorage;
    }
}
