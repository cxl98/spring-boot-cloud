package com.cxl.elFinder.command;

import com.cxl.elFinder.support.concurrency.GenericCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CommandFactory implements ElfinderCommandFactor{
    private static final Logger LOGGER= LoggerFactory.getLogger(CommandFactory.class);

    private String classNamePattern;

    private final GenericCache<String,ElFinderCommand> cache=new GenericCache<>();
    @Override
    public ElFinderCommand get(String commandName) {
        if (null==commandName||commandName.isEmpty()) {
            LOGGER.error(String.format("Command %s cannot be null or empty",commandName));
            throw new RuntimeException(String.format("Command %s cannot be null or empty", commandName));
        }
        ElFinderCommand command=null;
        try {
            command=cache.getValue(commandName, () -> {
                LOGGER.debug(String.format("trying recovery command!: %s",commandName));
                String className=String.format(getClassNamePattern(),commandName.substring(0,1).toUpperCase()+commandName.substring(1));
                return (ElFinderCommand) Class.forName(className).newInstance();
            });
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Unable to get/create command instance.", e);
        }
        return command;
    }

    private String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }
}
