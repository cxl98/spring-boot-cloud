package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;

public class RenameCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);
        final String newName=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_NAME);

        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
        VolumeHandler destination=new VolumeHandler(volumeHandler.getParent(),newName);
        volumeHandler.renameTo(destination);

        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED,new Object[]{getTargetInfo(request,destination)});
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_REMOVED,new String[]{target});
    }
}
