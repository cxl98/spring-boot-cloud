package com.cxl.elFinder.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements ElFinderCommand{
    private static final Logger LOGGER= LoggerFactory.getLogger(AbstractCommand.class);

    private static final String CMD_TMB_TARGET="?cmd=tmb&target=%s";
    private Map<String,Object> options=new HashMap<>();


}
