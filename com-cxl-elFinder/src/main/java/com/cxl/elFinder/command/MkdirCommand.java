package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MkdirCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        String target = request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);
        String dirName = request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_NAME);

        if (null != dirName) {
            VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
            VolumeHandler directory = new VolumeHandler(volumeHandler, dirName);
            directory.createFolder();
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED, new Object[]{getTargetInfo(request, directory)});
        } else {
            String[] param = request.getParameterMap().get(ElFinderConstansts.ELFINDER_PARAMETER_DIRS);
            List<Map> list = new ArrayList<>();
            for (String dir : param) {
                VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
                VolumeHandler directory = new VolumeHandler(volumeHandler, dir);
                directory.createFolder();
                list.add(getTargetInfo(request, directory));
            }
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED, list);

        }

    }
}
