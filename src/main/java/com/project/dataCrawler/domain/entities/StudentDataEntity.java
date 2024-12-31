package com.project.dataCrawler.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "studentdata")
public class StudentDataEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "roll_no", length = 15)
    private String rollNo;
    
    @Column(name = "enrollment_no", length = 10)
    private String enrollmentNo;
    
    @Column(name = "dob", length = 15)
    private String dob;
    
    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "mobile_no", length = 15)
    private String mobileNo;
    
    @Column(name = "email_id", length = 100)
    private String emailId;
    
    @Column(name = "father_mother_name", length = 100)
    private String fatherMother;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "standard", length = 5)
    private String standard;
    
    @Column(name = "centre", length = 250)
    private String centre;
    
    @Column(name = "astronomy_subject", length = 100)
    private String astronomySubject;
    
    @Column(name = "biology_subject", length = 100)
    private String biologySubject;
    
    @Column(name = "chemistry_subject", length = 100)
    private String chemistrySubject;
    
    @Column(name = "physics_subject", length = 100)
    private String physicsSubject;
    
    @Column(name = "astronomy_score", length = 5)
    private String astronomyScore;
    
    @Column(name = "biology_score", length = 5)
    private String biologyScore;
    
    @Column(name = "chemistry_score", length = 5)
    private String chemistryScore;
    
    @Column(name = "physics_score", length = 5)
    private String physicsScore;
    
}
