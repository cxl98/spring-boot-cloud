package com.cxl.elFinder.command;


import com.cxl.elFinder.core.ElFinderContext;

public interface ElFinderCommand {
    void execute(ElFinderContext context) throws Exception;
}
