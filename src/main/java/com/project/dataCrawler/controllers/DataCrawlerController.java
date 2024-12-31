package com.project.dataCrawler.controllers;

import com.project.dataCrawler.domain.dtos.StudentDataDto;
import com.project.dataCrawler.services.DataCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crawler")
public class DataCrawlerController {
    
    @Autowired
    private DataCrawlerService dataCrawlerService;
    
    @PostMapping(path = "/fetch")
    public ResponseEntity<StudentDataDto> fetchAndSaveDetails(
            @RequestParam String rollNo,
            @RequestParam String dob
    ) {
        return dataCrawlerService.fetchAndSaveData(rollNo, dob);
    }
    
}
