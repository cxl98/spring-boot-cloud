package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RmCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        String[] targets=request.getParameterValues(ElFinderConstansts.ELFINDER_PARAMETER_TARGETS);
        List<String> removed=new ArrayList<>();
        for (String target: targets) {
            VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
            try {
                volumeHandler.delete();
                removed.add(volumeHandler.getHash());
            } catch (IOException e) {
                jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ERROR,"Directory not empty");
            }
        }
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_REMOVED,removed.toArray());
    }
}
