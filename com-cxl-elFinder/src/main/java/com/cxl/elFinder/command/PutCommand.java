package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;

public class PutCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        VolumeHandler file=findTarget(elfinderStorage,target);
        OutputStream outputStream=file.openOutputStream();
        IOUtils.write(request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_CONTENT),outputStream,"UTF-8");
        outputStream.close();
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_CHANGED,new Object[]{getTargetInfo(request,file)});
    }
}
