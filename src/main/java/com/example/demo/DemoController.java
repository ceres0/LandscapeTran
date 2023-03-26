package com.example.demo;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.UploadedFile;

import java.io.*;

@Controller
public class DemoController {
    @Mapping("test")
    void test() throws IOException, InterruptedException {
        String exe = "python";
        String command = "src/main/python/test.py";
        String[] cmdArr = new String[] {exe, command};
        Process process = Runtime.getRuntime().exec(cmdArr);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        process.waitFor();
    }
}