package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DuplicateCommand extends AbstractJsonCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstansts.ELFINDER_PARAMETER_TARGETS);
        List<VolumeHandler> add = new ArrayList<>();
        for (String target : targets) {
            final VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
            final String name = volumeHandler.getName();
            String baseName = FilenameUtils.getBaseName(name);
            final String extension = FilenameUtils.getExtension(name);
            int i = 1;
            VolumeHandler newVolume;
            baseName = baseName.replaceAll("\\(\\d+\\)$", "");
            while (true) {

                String e = (null == extension || extension.isEmpty()) ? "" : "." + extension;
                String newName = String.format("%s(%d)%s", baseName, i, e);
                newVolume = new VolumeHandler(volumeHandler.getParent(), newName);
                if (!newVolume.exists()) {
                    break;
                }
                i++;
            }
            createAndCopy(volumeHandler,newVolume);
            add.add(newVolume);
        }
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED,buildJsonFilesArray(request,add));
    }
}
