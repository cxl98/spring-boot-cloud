package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PasteCommand extends AbstractJsonCommand implements ElFinderCommand {
    public static final String INT_CUT = "1";

    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstansts.ELFINDER_PARAMETER_TARGETS);
        final String destination = request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_FILE_DESTINATION);
        final boolean cut = INT_CUT.equals(request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_CUT));

        List<VolumeHandler> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();

        VolumeHandler volumeHandler = findTarget(elfinderStorage, destination);
        for (String target : targets) {
            VolumeHandler vh = findTarget(elfinderStorage, target);
            final String name = vh.getName();
            VolumeHandler newFile = new VolumeHandler(volumeHandler, name);
            createAndCopy(vh, newFile);
            added.add(newFile);

            if (cut) {
                vh.delete();
                removed.add(vh.getHash());
            }
        }
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED,buildJsonFilesArray(request,added));
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_REMOVED,removed.toArray());
    }
}
