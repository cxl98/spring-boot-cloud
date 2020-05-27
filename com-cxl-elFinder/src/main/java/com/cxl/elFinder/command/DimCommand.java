package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

public class DimCommand extends AbstractJsonCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        BufferedImage image;
        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);
        image= ImageIO.read(volumeHandler.openInputStream());
        jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_DIM,image.getWidth()+"x"+image.getHeight());
    }
}
