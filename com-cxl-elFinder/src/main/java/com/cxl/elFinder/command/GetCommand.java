package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

public class GetCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);
        final VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
        final InputStream inputStream=volumeHandler.openInputStream();
        final String content= IOUtils.toString(inputStream,"UTF-8");
        inputStream.close();
        jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_CONTENT,content);
    }
}
