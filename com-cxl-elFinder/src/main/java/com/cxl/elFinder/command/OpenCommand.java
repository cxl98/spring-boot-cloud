package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OpenCommand extends AbstractJsonCommand implements ElFinderCommand {
    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        boolean init=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_INIT)!=null;
        boolean tree=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TREE)!=null;
        String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files=new HashMap<>();
        if (init){
            jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_API,"1.0");
            jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_NETDRIVERS,new Object[0]);
        }
        if (tree){
            for (Volume volume:elfinderStorage.getVolumes()) {
                VolumeHandler root=new VolumeHandler(volume.getRoot(),elfinderStorage);
                files.put(root.getHash(),root);
                addSubFolders(files,root);
            }
        }

        VolumeHandler pwd=finaPwd(elfinderStorage,target);
        files.put(pwd.getHash(),pwd);
        addChildren(files,pwd);

        Object[] objects=buildJsonFilesArray(request,files.values());
        jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_FILES,objects);

        String hash=pwd.getHash();

        for (Object obj: objects) {
            ConcurrentHashMap<String,Object> map= (ConcurrentHashMap<String, Object>) obj;

            String strHash=map.get("hash").toString();
            if (hash.equals(strHash)){
                jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_CWD,map);
                break;
            }
        }
        jsonObject.put(ElFinderConstansts.ELFINDER_PARAMETER_OPTIONS,getOptions(pwd));
    }
}
