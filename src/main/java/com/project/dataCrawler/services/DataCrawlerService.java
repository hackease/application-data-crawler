package com.project.dataCrawler.services;

import com.project.dataCrawler.domain.dtos.StudentDataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataCrawlerService {
    
    ResponseEntity<StudentDataDto> fetchAndSaveData(String rollNo, String dob);
    
    List<String> getAllRollNo();
    
}
