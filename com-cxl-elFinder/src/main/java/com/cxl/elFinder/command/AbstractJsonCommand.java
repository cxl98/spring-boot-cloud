package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public abstract class AbstractJsonCommand extends AbstractCommand {
    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject json=new JSONObject();
        PrintWriter writer=response.getWriter();
        try {
            execute(elfinderStorage,request,json);
            response.setContentType("application/json; charset=UTF-8");
            writer.write(json.toJSONString());
            writer.flush();
        } catch (Exception e) {
            LOGGER.error("Unable to execute abstract json command", e);
            json.put(ElFinderConstansts.ELFINDER_JSON_RESPONSE_ERROR, e.getMessage());
            writer.write(json.toJSONString());
            writer.flush();
        }finally {
            if (null!=writer){
                writer.close();
            }
        }
    }
    abstract void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject)throws Exception;
}
