package com.example.demo;

import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.DownloadedFile;
import org.noear.solon.core.handle.UploadedFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Mapping("/api")
@Controller
public class ApiController {
    @Mapping("/hello")
    public String hello(@Param(defaultValue = "world") String name) {
        return String.format("Hello %s!", name);
    }

    @Post
    @Mapping("/upload")
    public String upload(UploadedFile file) throws IOException {
        String FileDir = System.getProperties().getProperty("user.home") + "/LandscapeTran/";
        String FileName = UUID.randomUUID() + "." + file.getExtension();
        File nfile = new File(FileDir);
        if(!nfile.exists()){
            boolean dr = nfile.mkdirs(); //创建目录
        }
        System.out.println(FileDir + FileName);
        file.transferTo(new File(FileDir + FileName)); // 把它转为本地文件
        return FileName;
    }

    @Get
    @Mapping("/down")
    public File down(String FileName) throws IOException {
        String FileDir = System.getProperties().getProperty("user.home") + "/LandscapeTran/";
        File nfile = new File(FileDir + FileName);
        return nfile;
    }

}