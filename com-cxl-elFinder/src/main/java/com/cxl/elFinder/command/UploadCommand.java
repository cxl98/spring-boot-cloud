package com.cxl.elFinder.command;

import com.alibaba.fastjson.JSONObject;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.utils.FileWriter;
import com.cxl.elFinder.utils.Part;
import com.cxl.elFinder.utils.Parts;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadCommand extends AbstractJsonCommand implements ElFinderCommand {

    @Override
    void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject jsonObject) throws Exception {
        List<FileItemStream> files = (List<FileItemStream>) request.getAttribute(FileItemStream.class.getName());

        List<VolumeHandler> added = new ArrayList<>();

        String target = request.getParameter("current");

        VolumeHandler parent = findTarget(elfinderStorage, target);

        FileWriter fileWriter = (fileName, is) -> {
            VolumeHandler newFile = new VolumeHandler(parent, fileName);

            newFile.createFile();


            OutputStream os = newFile.openOutputStream();
            IOUtils.copy(is, os);

            os.close();
            is.close();

            added.add(newFile);
            return newFile;
        };

        if (null != request.getParameter("cid")) {
            processChunKUpload(request, files, fileWriter);
        }else{
            processUpload(files,fileWriter);
        }
    }

    private void processUpload(List<FileItemStream> files, FileWriter fileWriter) throws IOException {

        for (FileItemStream item: files) {
            fileWriter.createAndSave(item.getName(),item.openStream());
        }
    }

    private void processChunKUpload(HttpServletRequest request, List<FileItemStream> files, FileWriter fileWriter) throws IOException {
        String cid = request.getParameter("cid");

        String chunk = request.getParameter("chunk");

        String range = request.getParameter("range");

        String[] tokens = range.split(",");

        Matcher m = Pattern.compile("(.*)\\.([0-9]+)_([0-9]+)\\.part")
                .matcher(chunk);

        if (m.find()) {
            String fileName = m.group(1);
            long index = Long.parseLong(m.group(2));
            long total = Long.parseLong(m.group(3));

            Parts parts = Parts.create(request, cid, fileName, total + 1,
                    Long.parseLong(tokens[2]));

            long start=Long.parseLong(tokens[0]);
            long size=Long.parseLong(tokens[1]);

            System.out.println(String.format("uploaded part(%d/%d) of file: %s",
                    index, total, fileName));

            parts.addPart(index,new Part(start,size,files.get(0)));
            String.format(">>>>%d", parts.getTotalSize());

            if (parts.isReady()){
                parts.checkParts();;

                String format = String.format("file is uploadded completely: %s",
                        fileName);
                System.out.println(format);

                fileWriter.createAndSave(fileName,parts.openInpurStream());
                parts.remove(request);
            }
        }
    }
}
