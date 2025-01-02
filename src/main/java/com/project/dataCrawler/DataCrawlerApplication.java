package com.project.dataCrawler;

import com.project.dataCrawler.config.ApplicationStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCrawlerApplication implements CommandLineRunner {
    
    @Autowired
    private ApplicationStarter applicationStarter;
    
    public static void main(String[] args) {
        SpringApplication.run(DataCrawlerApplication.class, args);
	}
    
    @Override
    public void run(String... args) {
        try {
            // Populate data (RollNo & DOB)
            applicationStarter.populateData();
            
            // Execute threads
            applicationStarter.startThreads();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
