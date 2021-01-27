package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.service.ElfinderStorage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SizeCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String[] targets=request.getParameterValues(ElFinderConstansts.ELFINDER_PARAMETER_TARGETS);

        List<Target> targetList=findTargets(elfinderStorage,targets);

       long size=0;
        for (Target target: targetList) {
            Volume volume=target.getVolume();
            size+=volume.getSize(target);
        }
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_SIZE,size);
    }
}
