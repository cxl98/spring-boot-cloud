package com.cxl.cloud.server;

import com.cxl.elFinder.command.ElFinderCommand;
import com.cxl.elFinder.command.ElfinderCommandFactor;
import com.cxl.elFinder.core.ElFinderContext;
import com.cxl.elFinder.service.ElfinderStorageFactory;
import com.cxl.elFinder.utils.ElFinderConstansts;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("elfinder/connector")
public class CloudController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudController.class);
    private static final String OPEN_STREAM = "openStream";
    private static final String PARAMETER = "getParameter";
    @Resource(name = "commandFactory")
    private ElfinderCommandFactor elfinderCommandFactor;

    @Resource(name = "storageFactory")
    private ElfinderStorageFactory elfinderStorageFactory;

    @RequestMapping
    public void connect(HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request = process(request);

        String cmd=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_COMMAND);
        ElFinderCommand elFinderCommand=elfinderCommandFactor.get(cmd);
        final HttpServletRequest protectedRequest=request;
        try {
            elFinderCommand.execute(new ElFinderContext() {
                @Override
                public ElfinderStorageFactory getVolumeSourceFactory() {
                    return elfinderStorageFactory;
                }

                @Override
                public HttpServletRequest getRequest() {
                    return protectedRequest;
                }

                @Override
                public HttpServletResponse getResponse() {
                    return response;
                }
            });
        } catch (Exception e) {
            LOGGER.error("Unknown error", e);
        }
    }

    private HttpServletRequest process(HttpServletRequest request) throws IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return request;
        }
        Map<String, String[]> map = request.getParameterMap();
        final Map<String, Object> requestParams = new HashMap<>();
        for (String key : map.keySet()) {
            String[] obj = map.get(key);
            if (1 == obj.length) {
                requestParams.put(key, obj[0]);
            } else {
                requestParams.put(key, obj);
            }
        }
        AbstractMultipartHttpServletRequest multipartHttpServletRequest = (AbstractMultipartHttpServletRequest) request;

        ServletFileUpload servletFileUpload = new ServletFileUpload();
        String encoding = request.getCharacterEncoding();
        if (null == encoding) {
            encoding = "UTF-8";
        }
        servletFileUpload.setHeaderEncoding(encoding);

        List<MultipartFile> files = multipartHttpServletRequest.getFiles("upload[]");
        List<FileItemStream> list = new ArrayList<>();

        for (MultipartFile file : files) {
            FileItemStream item = createFileTiemStream(file);
            InputStream stream = item.openStream();
            String fileName = item.getName();
            if (null != fileName &&!fileName.isEmpty()){
                ByteArrayOutputStream os=new ByteArrayOutputStream();
                IOUtils.copy(stream,os);
                final byte[] bs=os.toByteArray();
                stream.close();
                list.add((FileItemStream) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{FileItemStream.class},(proxy,method,args)->{
                    if (OPEN_STREAM.equals(method.getName())){
                        return new ByteArrayInputStream(bs);
                    }
                    return method.invoke(item,args);
                }));
            }
        }
        request.setAttribute(FileItemStream.class.getName(),list);
        return (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{HttpServletRequest.class},(proxy, method, args)->{
            if (PARAMETER.equals(method.getName())){
                return requestParams.get(args[0]);
            }
            return method.invoke(request,args);
        });
    }

    private FileItemStream createFileTiemStream(MultipartFile file) {
        return new FileItemStream() {
            @Override
            public InputStream openStream() throws IOException {
                return file.getInputStream();
            }

            @Override
            public String getContentType() {
                return file.getContentType();
            }

            @Override
            public String getName() {
                return file.getOriginalFilename();
            }

            @Override
            public String getFieldName() {
                return file.getName();
            }

            @Override
            public boolean isFormField() {
                return false;
            }

            @Override
            public FileItemHeaders getHeaders() {
                return null;
            }

            @Override
            public void setHeaders(FileItemHeaders fileItemHeaders) {

            }
        };
    }
}
