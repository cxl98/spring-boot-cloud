package com.cxl.elFinder.command;

import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import org.apache.commons.compress.utils.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 文件操作
 */
public class FileCommand extends AbstractCommand implements ElFinderCommand {
    private static final String STREAM="1";
    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target=request.getParameter("target");
        boolean download=STREAM.equals(request.getParameter("download"));
        VolumeHandler file=findTarget(elfinderStorage,target);
        String mime=file.getMimeType();
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType(mime);
        String fileName=file.getName();
        if (download){
            response.setHeader("Content-Disposition","attachments; "+getFileName(fileName,request.getHeader("USER-AGENT")));
            response.setHeader("Content-Transfer-Encoding", "binary");
        }
        OutputStream outputStream=response.getOutputStream();
        response.setContentLength((int) file.getSize());
        try(InputStream inputStream=file.openInputStream()){
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }

    private String getFileName(String fileName,String userAgent) throws UnsupportedEncodingException {
        if (userAgent!=null){
            userAgent=userAgent.toLowerCase();
            if (userAgent.contains("mozilla")) {
                return "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
            }
        }
        return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
    }
}
