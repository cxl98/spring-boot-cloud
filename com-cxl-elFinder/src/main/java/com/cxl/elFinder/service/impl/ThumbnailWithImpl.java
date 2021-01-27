package com.cxl.elFinder.service.impl;

import com.cxl.elFinder.service.ThumbnailWidth;

public class ThumbnailWithImpl implements ThumbnailWidth {
    private int thumbnailWidth;
    @Override
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
}
