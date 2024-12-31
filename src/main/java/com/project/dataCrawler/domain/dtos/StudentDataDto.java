package com.project.dataCrawler.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDataDto {
    
    private Long id;
    
    private String rollNo;
    
    private String enrollmentNo;
    
    private String dob;
    
    private String name;
    
    private String mobileNo;
    
    private String emailId;
    
    private String fatherMother;
    
    private String gender;
    
    private String standard;
    
    private String centre;
    
    private String astronomySubject;
    
    private String biologySubject;
    
    private String chemistrySubject;
    
    private String physicsSubject;
    
    private String astronomyScore;
    
    private String biologyScore;
    
    private String chemistryScore;
    
    private String physicsScore;
    
}
