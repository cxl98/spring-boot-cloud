package com.cxl.elFinder.config;

import java.net.URI;

public final class ElFinderConfigurationUtils {
    public ElFinderConfigurationUtils() {
    }

    public static String treatPath(String path) {
        if (null != path && !path.isEmpty()) {
            final String uriSuffix="file:";
            final String slash="/";

            path = path.replaceAll("//",slash);
            path=path.replaceAll("\\\\",slash);
            path=path.replaceAll("\\s+"," ");
            path=path.replaceAll("[\\p{Z}]","%20");

            StringBuffer sb=new StringBuffer();
            if (!path.startsWith(uriSuffix)) {
                if (path.startsWith(slash)) {
                    sb.append(uriSuffix).append(path);
                }else{
                    sb.append(uriSuffix).append(slash).append(path);
                }
            }
            return sb.toString();
        }
        return path;
    }

    public static URI toURI(String path){
        return URI.create(treatPath(path));
    }

}
