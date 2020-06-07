package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ParentsCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files=new HashMap<>();

        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);

        while(!volumeHandler.isRoot()){
            addChildren(files,volumeHandler);
            volumeHandler=volumeHandler.getParent();
        }

        jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_TREE,buildJsonFilesArray(request,files.values()));
    }
}
