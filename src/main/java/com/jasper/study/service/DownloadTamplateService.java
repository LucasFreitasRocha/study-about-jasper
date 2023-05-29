package com.jasper.study.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.URL;

@Service
public class DownloadTamplateService {
    public File downlaodTempalte(String templateName) {
        try {
            URL url = new URL("https://raw.githubusercontent.com/LucasFreitasRocha/templates-jasper-study/master/" + templateName);
            File file = ResourceUtils.getFile("classpath:templates/defaultTemplate.jrxml");
            FileUtils.copyURLToFile(url, file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
