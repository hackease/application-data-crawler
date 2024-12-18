package com.project.dataCrawler.controllers;

import com.project.dataCrawler.domain.dtos.UserDetailsDto;
import com.project.dataCrawler.services.DataCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawler")
public class DataCrawlerController {
    
    @Autowired
    private DataCrawlerService dataCrawlerService;
    
    @PostMapping(path = "/fetch")
    public ResponseEntity<UserDetailsDto> fetchAndSaveDetails(
            @RequestParam String regNo,
            @RequestParam String dob
    ) {
        return dataCrawlerService.fetchAndSaveDetails(regNo, dob);
    }
    
}
