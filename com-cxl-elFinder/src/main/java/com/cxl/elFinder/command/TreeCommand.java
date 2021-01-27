package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class TreeCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files=new HashMap<>();
        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
        addChildren(files,volumeHandler);

        jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_TREE,buildJsonFilesArray(request,files.values()));
    }
}
