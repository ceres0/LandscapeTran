package com.example.demo;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.FileStaticRepository;

@SolonMain
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args, app -> {
            StaticMappings.add("/img/", new FileStaticRepository(System.getProperties().getProperty("user.home") + "/LandscapeTran/"));
        });
    }
}