package com.cxl.elFinder.config;

import com.cxl.elFinder.command.CommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ElFinderConfig {
    @Resource
    private ElfinderConfiguration elFinderConfiguration;

    @Bean(name = "commandFactory")
    public CommandFactory getCommandFactory(){
        CommandFactory commandFactory=new CommandFactory();
        commandFactory.setClassNamePattern(elFinderConfiguration.getCommand()+".%Command");
        return commandFactory;
    }

}
