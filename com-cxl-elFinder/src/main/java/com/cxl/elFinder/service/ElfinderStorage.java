package com.cxl.elFinder.service;

import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeSecurity;

import java.io.IOException;
import java.util.List;

public interface ElfinderStorage {
    Target fromHash(String hash);
    String getHash(Target target) throws IOException;

    String getVolumeId(Volume volume);
   VolumeSecurity getVolumeSecurity(Target target);

   List<Volume> getVolumes();

   List<VolumeSecurity> getVolumeSecurities();

}
