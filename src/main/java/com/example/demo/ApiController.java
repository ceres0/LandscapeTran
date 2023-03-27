package com.example.demo;

import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.DownloadedFile;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.handle.UploadedFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String Upload(UploadedFile file) throws IOException {
        String FileDir = System.getProperties().getProperty("user.home") + "/LandscapeTran/";
//        String FileDir = "src/main/resources/static/img/";
        String FileName = UUID.randomUUID() + "." + file.getExtension();
        File nfile = new File(FileDir);
        if(!nfile.exists()){
            boolean dr = nfile.mkdirs(); //创建目录
        }
        System.out.println(FileDir + FileName);
        file.transferTo(new File(FileDir + FileName)); // 把它转为本地文件
        return FileName;
    }

    @Mapping("tran")
    public String Tran(String fileName){
        // 将图片大小调整为模型所需大小，并转存到模型输入路径
        String FileDir = System.getProperties().getProperty("user.home") + "/LandscapeTran/";
        String ext = fileName.split("\\.")[1];
//        String ext = fileName.split("\\.")[1];
        File file = new File(FileDir + fileName);
        BufferedImage image;
        try{
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        Image resizedImage = image.getScaledInstance(256, 256, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resizedImage, 0, 0, null);
        try {
            // 使用ImageIO类的write()方法，传入一个BufferedImage对象，一个文件格式的字符串，和一个File对象作为参数
            ImageIO.write(outputImage, ext, new File("src/main/python/datasets/photo2ink/test/A/ori.jpg"));
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }

//        Path source = Paths.get(FileDir + fileName);
//        Path target = Paths.get("src/main/python/datasets/photo2ink/test/A/ori." + ext);
        // 调用模型生成的python脚本
        try{
            System.out.println("test");
            String exe = "python";
//            String exe = "/usr/local/Caskroom/miniconda/base/envs/LandscapeTran/bin/python";
            String command = "src/main/python/test.py";
            String[] cmdArr = new String[] {exe, command, "src/main/python/"};
            Process process = Runtime.getRuntime().exec(cmdArr);
//            Process process = Runtime.getRuntime().exec(exe + " " + command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            process.waitFor();
        } catch (IOException | InterruptedException e){
            System.out.println(e);
            e.printStackTrace();
        }

        // 将生成好的文件转存到文件路径
        String FileName = UUID.randomUUID() + ".png";
        Path source = Paths.get("src/main/python/output/B/0001.png");
        Path target = Paths.get(FileDir + FileName);
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }

        return FileName;
    }

    @Get
    @Mapping("/down")
    public File Down(String FileName) throws IOException {
        String FileDir = System.getProperties().getProperty("user.home") + "/LandscapeTran/";
        File nfile = new File(FileDir + FileName);
        return nfile;
    }

}