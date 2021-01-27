package com.cxl.elFinder.command;

import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.core.ElFinderContext;
import com.cxl.elFinder.core.Target;
import com.cxl.elFinder.exception.ElFinderException;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import com.cxl.elFinder.support.archiver.ArchiveOption;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.*;

public abstract class AbstractCommand implements ElFinderCommand {
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

    private static final String CMD_TMB_TARGET = "?cmd=tmb&target=%s";
    private Map<String, Object> options = new HashMap<>();

    /**
     * 添加子文件
     *
     * @param map    　String 子文件key ｖａｌｕｅ　：VolumeHandler里的方法
     * @param target 需要操作文件目标
     *
     */
    protected void addChildren(Map<String, VolumeHandler> map, VolumeHandler target) throws IOException {
        for (VolumeHandler item : target.listChildren()) {
            map.put(item.getHash(), item);
        }
    }

    /**
     * 添加子文件夹
     *
     * @param map    　String 子文件夹key ｖａｌｕｅ　：VolumeHandler里的方法
     * @param target 需要操作文件夹目标
     *
     */
    protected void addSubFolders(Map<String, VolumeHandler> map, VolumeHandler target) throws IOException {
        for (VolumeHandler item : target.listChildren()) {
            if (item.isFolder()) {
                map.put(item.getHash(), item);
            }
        }
    }

    protected void createAndCopy(VolumeHandler src, VolumeHandler volumeHandler) throws IOException {
        if (src.isFolder()) {
            createAndCopyFolder(src, volumeHandler);
        } else {
            createAndCopyFile(src, volumeHandler);
        }
    }

    private void createAndCopyFile(VolumeHandler src, VolumeHandler volumeHandler) throws IOException {
        InputStream is = null;
        OutputStream out = null;
        try {
            volumeHandler.createFile();
            is = src.openInputStream();
            out = src.openOutputStream();
            IOUtils.copy(is, out);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (null != is) {

                is.close();
            }
            if (null != out) {

                out.close();
            }
        }

    }

    private void createAndCopyFolder(VolumeHandler src, VolumeHandler volumeHandler) throws IOException {
        volumeHandler.createFolder();
        for (VolumeHandler item : src.listChildren()) {
            if (item.isFolder()) {

                createAndCopyFolder(item, new VolumeHandler(volumeHandler, item.getName()));
            } else {
                createAndCopyFile(item, new VolumeHandler(volumeHandler, item.getName()));
            }
        }
    }

    @Override
    public void execute(ElFinderContext context) throws Exception {
        ElfinderStorage elfinderStorage = context.getVolumeSourceFactory().getVolumeSource();
        execute(elfinderStorage, context.getRequest(), context.getResponse());
    }

    public abstract void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected Object[] buildJsonFilesArray(HttpServletRequest request, Collection<VolumeHandler> list) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(list.size());
        List<Map<String, Object>> jsonFile = new ArrayList<>();

        for (VolumeHandler item : list) {
            executor.execute(new Tasks(jsonFile, request, item, latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
        return jsonFile.toArray();
    }

    /**
     * 绝对路径pwd
     */
    protected VolumeHandler finaPwd(ElfinderStorage elfinderStorage,String target){
        VolumeHandler pwd=null;
        if (null!=target&&!"".equals(target)) {
            pwd=findTarget(elfinderStorage,target);
        }
        if (null==pwd) {
            pwd=new VolumeHandler(elfinderStorage.getVolumes().get(0).getRoot(),elfinderStorage);
        }
        return pwd;
    }

    protected  VolumeHandler findTarget(ElfinderStorage elfinderStorage, String target){
        Target target1=elfinderStorage.fromHash(target);
        if (null==target1) {
           throw new ElFinderException("没有该文件或目录");
        }
        return new VolumeHandler(target1,elfinderStorage);
    }

    protected List<Target> findTargets(ElfinderStorage elfinderStorage,String[] targetHashes){
        if (null!=elfinderStorage&&null!=targetHashes){
            List<Target> targets=new ArrayList<>(targetHashes.length);
            for (String item: targetHashes) {
                Target target=elfinderStorage.fromHash(item);
                if (null!=target){
                    targets.add(target);
                }

            }
            return targets;
        }
        return Collections.emptyList();
    }

    protected Map<String,Object> getTargetInfo(final HttpServletRequest request,final VolumeHandler target)throws IOException{
        Map<String,Object> info=new ConcurrentHashMap<>();

        info.put(ElFinderConstansts.ELFINDER_PARAMETER_HASH,target.getHash());
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_MIME,target.getMimeType());
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_TIMESTAMP,target.getLastModified());
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_SIZE,target.getSize());
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_READ,target.isReadable()?ElFinderConstansts.ELFINDER_TRUE_RESPONSE:ElFinderConstansts.ELFINDER_FALSE_RESPONSE);
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_WRITE,target.isWritable()?ElFinderConstansts.ELFINDER_TRUE_RESPONSE:ElFinderConstansts.ELFINDER_FALSE_RESPONSE);
        info.put(ElFinderConstansts.ELFINDER_PARAMETER_LOCKED,target.isLocked()?ElFinderConstansts.ELFINDER_TRUE_RESPONSE:ElFinderConstansts.ELFINDER_FALSE_RESPONSE);

        if (null!=target.getMimeType()&&target.getMimeType().startsWith("image")){
            StringBuffer buffer=request.getRequestURL();
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_THUMBNAIL,buffer.append(String.format(CMD_TMB_TARGET,target.getHash())));
        }
        if (target.isRoot()) {
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_DIRECTORY_FILE_NAME,target.getVolumeAlias());
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_VOLUME_ID,target.getVloumeId());
        }else{
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_DIRECTORY_FILE_NAME,target.getName());
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_PARENTHASH,target.getParent().getHash());
        }
        if (target.isFolder()){
            info.put(ElFinderConstansts.ELFINDER_PARAMETER_HAS_DIR,target.hasChildFolder()?ElFinderConstansts.ELFINDER_TRUE_RESPONSE:ElFinderConstansts.ELFINDER_FALSE_RESPONSE);
        }
        return info;
    }

    protected Map<String,Object> getOptions(VolumeHandler pwd){
        String[] array={};
        options.put(ElFinderConstansts.ELFINDER_PARAMETER_PATH,pwd.getName());
        options.put(ElFinderConstansts.ELFINDER_PARAMETER_COMMAND_DISABLED,array);
        options.put(ElFinderConstansts.ELFINDER_PARAMETER_FILE_SEPARATOR,ElFinderConstansts.ELFINDER_PARAMETER_FILE_SEPARATOR);
        options.put(ElFinderConstansts.ELFINDER_PARAMETER_OVERWRITE_FILE,ElFinderConstansts.ELFINDER_TRUE_RESPONSE);
        options.put(ElFinderConstansts.ELFINDER_PARAMETER_ARCHIVERS, ArchiveOption.JSON_INSTANCE());
        return options;
    }

    class Tasks implements Runnable {
        private List<Map<String, Object>> jsonFile;
        private HttpServletRequest request;
        private VolumeHandler volumeHandler;
        private CountDownLatch countDownLatch;

        Tasks(List<Map<String, Object>> jsonFile, HttpServletRequest request, VolumeHandler volumeHandler, CountDownLatch countDownLatch) {
            this.jsonFile = jsonFile;
            this.request = request;
            this.volumeHandler = volumeHandler;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                jsonFile.add(getTargetInfo(request,volumeHandler));
                countDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
