package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import com.cxl.elFinder.support.archiver.ArchiveType;
import com.cxl.elFinder.support.archiver.Archiver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class ArchiveCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final  String[] targets=request.getParameterValues(ElFinderConstansts.ELFINDER_PARAMETER_TARGETS);
        final String type=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TYPE);
        List<Target> targetList=findTargets(elfinderStorage,targets);

        try {
            Archiver archiver= ArchiveType.metch(type).getStrategy();

            Target target=archiver.compress(targetList.toArray(new Target[0]));
            Object[] archiveInfo={
                    getTargetInfo(request,new VolumeHandler(target,elfinderStorage))
            };
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED,archiveInfo);
        } catch (IOException e) {
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ERROR, "Unable to create the archive! Error: " + e);
        }
    }
}
