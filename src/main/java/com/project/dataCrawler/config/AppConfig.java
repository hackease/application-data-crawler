package com.project.dataCrawler.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppConfig {
    
    @Value("${app.file.pdfFolderPath}")
    private String pdfFolderPath;
    
}
