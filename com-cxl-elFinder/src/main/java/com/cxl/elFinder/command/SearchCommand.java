package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeSecurity;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class SearchCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        final String query = request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_SEARCH_QUERY);

        try {
            List<Object> objects = null;
            List<Volume> volumes = elfinderStorage.getVolumes();

            for (Volume volume : volumes) {

                // checks volume security
                Target target = volume.getRoot();
                VolumeSecurity volumeSecurity = elfinderStorage.getVolumeSecurity(target);

                // search only in volumes that are readable
                if (volumeSecurity.getSecurityConstraint().isReadable()) {
                    // search for targets
                    List<Target> targets = volume.search(query);

                    if (null != targets) {

                        //初始化列表
                        if (null == objects) {
                            objects = new ArrayList<>(targets.size());
                        }

                        //在list 返回　targets 信息

                        for (Target item : targets) {
                            objects.add(getTargetInfo(request, new VolumeHandler(item, elfinderStorage)));
                        }
                    }
                }

                Object[] array = objects != null ? objects.toArray() : new Object[0];

                jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_FILES, array);


            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ERROR,"目标不存在,：　"+e);
        }

    }
}
