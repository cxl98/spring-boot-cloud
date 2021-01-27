package com.nio.test;

import com.cxl.elFinder.utils.ElFinderConfigurationUtils;

import java.net.URI;

public class ElFinderConfigurationUtilsTest {
    public static void main(String[] args) {
        URI uri = ElFinderConfigurationUtils.toURI("/cxl/home");
        System.out.println(uri);
    }
}
