package com.cxl.cloud.common.factory;

import com.cxl.elFinder.command.ElFinderCommand;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface CommandFactory {
    ElFinderCommand get(String commandName);
}
