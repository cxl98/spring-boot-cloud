package com.cxl.cloud.common.config;

import com.cxl.cloud.common.factory.impl.CommandFactoryImpl;
import com.cxl.elFinder.core.Volume;
import com.cxl.elFinder.core.VolumeSecurity;
import com.cxl.elFinder.core.impl.DefaultVolumeSecurity;
import com.cxl.elFinder.core.impl.SecurityConstant;
import com.cxl.elFinder.param.Node;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.ElfinderStorageFactory;
import com.cxl.elFinder.service.VolumeSource;
import com.cxl.elFinder.service.impl.ElfinderStorageimpl;
import com.cxl.elFinder.service.impl.ThumbnailWithImpl;
import com.cxl.elFinder.support.local.LocalUtils;
import com.cxl.elFinder.utils.ElFinderConstansts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class BeanConfig {
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean(name = "commandFactory")
    public CommandFactoryImpl getCommandFactory(){
        CommandFactoryImpl commandFactory=new CommandFactoryImpl();
        commandFactory.setClassNamePattern(propertiesConfig.getCommand()+".%Command");
        return commandFactory;
    }

    @Bean(name = "storageFactory")
    public ElfinderStorageFactory getStorageFactory(){
        com.cxl.elFinder.service.impl.ElfinderStorageFactory elfinderStorageFactory=new com.cxl.elFinder.service.impl.ElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getStorage());
        return elfinderStorageFactory;
    }

    @Bean(name = "Storage")
    public ElfinderStorage getStorage(){
        ElfinderStorageimpl elfinderStorageimpl=new ElfinderStorageimpl();

        // creates thumbnail
        ThumbnailWithImpl thumbnailWith=new ThumbnailWithImpl();
        thumbnailWith.setThumbnailWidth(propertiesConfig.getThumbnail().getWith().intValue());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character id='A';
        List<Node> elfinderConfigurationVolumes=propertiesConfig.getVolumes();

        List<Volume> volumes=new ArrayList<>(elfinderConfigurationVolumes.size());

        Map<Volume,String> ids=new HashMap<>(elfinderConfigurationVolumes.size());

        Map<Volume, Locale> elfinderVolumeLocales=new HashMap<>(elfinderConfigurationVolumes.size());

        List<VolumeSecurity> elfinderVolumeSecurities=new ArrayList<>();

        // creates volumes
        for (Node item: elfinderConfigurationVolumes) {
            String alias=item.getAlias();
            String path=item.getPath();
            String source=item.getSource();
            String locale=item.getLocale();
            final boolean isLocked=item.getConstraint().isLocked();
            final boolean isReadable=item.getConstraint().isReadable();
            final boolean isWritable=item.getConstraint().isWritable();

            // creates new volume
            Volume volume= VolumeSource.source(source).newInstance(alias,path);
            volumes.add(volume);

            ids.put(volume,Character.toString(id));
            elfinderVolumeLocales.put(volume, LocalUtils.toLocale(locale));

            // creates security constraint
            SecurityConstant securityConstant=new SecurityConstant();
            securityConstant.setLocked(isLocked);
            securityConstant.setReadable(isReadable);
            securityConstant.setWritable(isWritable);

            // creates volume pattern and volume security
            final String volumePattern=Character.toString(id)+ ElFinderConstansts.ELFINDER_SERCURITY_REGEX;
            elfinderVolumeSecurities.add(new DefaultVolumeSecurity(volumePattern,securityConstant));

            // prepare next volumeId character
            id++;
        }
        elfinderStorageimpl.setThumbnailWidth(thumbnailWith);
        elfinderStorageimpl.setVolumes(volumes);
        elfinderStorageimpl.setVolumeIds(ids);
        elfinderStorageimpl.setVolumeLocales(elfinderVolumeLocales);

        elfinderStorageimpl.setVolumeSecurities(elfinderVolumeSecurities);

        return elfinderStorageimpl;
    }

}
