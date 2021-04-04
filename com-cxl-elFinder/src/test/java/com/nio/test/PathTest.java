package com.nio.test;


import com.cxl.elFinder.support.nio.NioFileSystem;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PathTest {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("/home/cxl/cxl/test");
        NioFileSystem.createFolder(path);
        NioFileSystem.createFile(Paths.get("/home/cxl/cxl/test/11.txt"));

        boolean same = NioFileSystem.isSame(path, Paths.get("/home/cxl/cxl/test"));
        System.out.println(same);
        OutputStream outputStream = NioFileSystem.openOutputStream(Paths.get("/home/cxl/cxl/test/11.txt"));
        String s="你好";
        outputStream.write(s.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        System.out.println(new String(s.getBytes()));
        InputStream inputStream = NioFileSystem.openInputStream(Paths.get("/home/cxl/cxl/test/11.txt"));
        byte[] bytes=new byte[1024];
        int read = inputStream.read(bytes);
        System.out.println(new String(bytes,0,read));
        inputStream.close();
        //可以进行搜索
        List<Path> test = NioFileSystem.search(Paths.get("/home/cxl/cxl"),"11");
        for (Path path1:test){
            System.out.println(path1.toString());
        }

//        Path path = Paths.get("/home/cxl/cxl/test");
//        NioSystem.createFolder(path);
//        NioSystem.createFile(Paths.get("/home/cxl/cxl/test/11.txt"));
//
//        boolean same = NioSystem.isSame(path, Paths.get("/home/cxl/cxl/test"));
//        System.out.println(same);
//        OutputStream outputStream = NioSystem.openOutputStream(Paths.get("/home/cxl/cxl/test/11.txt"));
//        String s="你好";
//        outputStream.write(s.getBytes("UTF-8"));
//        outputStream.flush();
//        outputStream.close();
//        System.out.println(new String(s.getBytes()));
//        InputStream inputStream = NioSystem.openInputStream(Paths.get("/home/cxl/cxl/test/11.txt"));
//        byte[] bytes=new byte[1024];
//        int read = inputStream.read(bytes);
//        System.out.println(new String(bytes,0,read));
//        inputStream.close();
//        //可以进行搜索
//        List<Path> test = NioSystem.search(Paths.get("/home/cxl/cxl"),"11");
//        for (Path path1:test){
//            System.out.println(path1.toString());
//        }

        Path path1 = Paths.get(System.getProperty("user.home"));
        System.out.println(path1.toString());
    }
}
