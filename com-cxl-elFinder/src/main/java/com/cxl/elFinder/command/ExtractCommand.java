package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import com.cxl.elFinder.support.archiver.ArchiveType;
import com.cxl.elFinder.support.archiver.Archiver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解压缩文件
 */
public class ExtractCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String target = request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        Target targetCompressed = elfinderStorage.fromHash(target);
        Volume targetCompressedVolume = targetCompressed.getVolume();
        String mimeType = targetCompressedVolume.getMimeType(targetCompressed);
        try {
            Archiver archiver = ArchiveType.metch(mimeType).getStrategy();
            Target decompressTarget = archiver.decompress(targetCompressed);

            Object[] archiveInfo = {getTargetInfo(request, new VolumeHandler(decompressTarget, elfinderStorage))};
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ADDED, archiveInfo);
        } catch (IOException e) {
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ERROR, "Unable to extract the archive! Error: " + e);
        }

    }
}
