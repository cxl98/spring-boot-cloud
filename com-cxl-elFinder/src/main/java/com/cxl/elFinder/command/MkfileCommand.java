package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;

public class MkfileCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
       final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);
        final String fileName=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_NAME);

        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
        VolumeHandler newFile=new VolumeHandler(volumeHandler,fileName);
        newFile.createFile();
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED,new Object[]{getTargetInfo(request,newFile)});
    }
}
