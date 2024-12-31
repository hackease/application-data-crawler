package com.project.dataCrawler.services.impl;

import com.project.dataCrawler.domain.dtos.StudentDataDto;
import com.project.dataCrawler.domain.entities.StudentDataEntity;
import com.project.dataCrawler.mappers.StudentDataMapper;
import com.project.dataCrawler.repositories.StudentDataRepository;
import com.project.dataCrawler.services.DataCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataCrawlerServiceImpl implements DataCrawlerService {
    
    private static final String URL = "https://www.example.com";
    
    @Autowired
    private StudentDataRepository studentDataRepository;
    
    @Autowired
    private StudentDataMapper studentDataMapper;
    
    @Override
    public ResponseEntity<StudentDataDto> fetchAndSaveData(String rollNo, String dob) {
        
        try {
            // Establish a connection and fetch the GET response
            Connection.Response getStudentLogin = Jsoup.connect(URL+"/Login")
                                                          .method(Connection.Method.GET)
                                                          .execute();
            
            // Extract cookies from the response
            Map<String, String> cookies = getStudentLogin.cookies();
            
            // Fetch the get webpage
            Document getStudentLoginDocument = getStudentLogin.parse();
            
            // Locate the input element of __RequestVerificationToken
            Element requestVerificationTokenInputElement = getStudentLoginDocument.selectFirst("input[name=__RequestVerificationToken]");
            
            // Extract the __RequestVerificationToken of the form
            String requestVerificationToken = requestVerificationTokenInputElement != null ? requestVerificationTokenInputElement.attr("value") : "";
            
            // URL-encoded key-value pairs
            Map<String, String> formData = new HashMap<>();
            formData.put("__RequestVerificationToken", requestVerificationToken);
            formData.put("RegNo", rollNo);
            formData.put("DOB", dob);
            
            // Submit the form with the required data and fetch the POST response
            Connection.Response postRollNoLogin = Jsoup.connect(URL+"/Login/RollNoLogin")
                                                          .cookies(cookies)
                                                          .data(formData)
                                                          .method(Connection.Method.POST)
                                                          .execute();
            
            // Fetch the post Webpage
            Document dashboardDocument = postRollNoLogin.parse();
            
            Element dashboardElement = dashboardDocument.selectFirst("h3:containsOwn(DASHBOARD)");
            Element invalidLoginElement = dashboardDocument.selectFirst("strong:containsOwn(Invalid Login Credentials!)");
            
            StudentDataEntity studentData = new StudentDataEntity();
            
            if (dashboardElement == null && invalidLoginElement != null) {
                if (dob.equals("31/12/2011")) {
                    // Save RollNo into the database
                    studentData.setRollNo(rollNo);
                    // Set DOB to "Not in range" into the database
                    studentData.setDob("Not in range");
                    
                    studentDataRepository.save(studentData);
                }
                
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // Fetch all label elements from the Internal Dashboard Webpage
                Elements dashboardLabelElements = dashboardDocument.select("label");
                
                // Save Internal Dashboard data into the database
                studentData.setRollNo(rollNo);
                studentData.setEnrollmentNo(dashboardLabelElements.get(1).text());
                studentData.setDob(dob);
                studentData.setName(dashboardLabelElements.get(5).text());
                studentData.setMobileNo(dashboardLabelElements.get(8).text());
                studentData.setEmailId(dashboardLabelElements.get(10).text());
                
                // Fetch the GET response for the Score-card
                Connection.Response getScoreCard = Jsoup.connect(URL+"/Application/ScoreCard")
                                                           .cookies(cookies)
                                                           .method(Connection.Method.GET)
                                                           .execute();
                
                // Fetch the Scorecard Webpage
                Document scoreCardDocument = getScoreCard.parse();
                
                // Fetch all label elements from the ScoreCard Webpage
                Elements scoreCardH4LabelElements = scoreCardDocument.select("h4");
                Elements scoreCardH5LabelElements = scoreCardDocument.select("h5");
                
                int[] h4Index = {4, 6, 8, 14};
                String[] h4Fields = new String[4];
                
                for (int i=0; i<h4Index.length; i++) {
                    Element nextLabelElement = scoreCardH4LabelElements.get(h4Index[i]).selectFirst("label");
                    
                    if (nextLabelElement != null) h4Fields[i] = nextLabelElement.text();
                    else h4Fields[i] = null;
                }
                
                String[] h5Fields = new String[8];
                
                for (int i=0; i<h5Fields.length; i++) {
                    Element nextLabelElement = scoreCardH5LabelElements.get(i).selectFirst("label");
                    
                    if (nextLabelElement != null) h5Fields[i] = nextLabelElement.text();
                    else h5Fields[i] = null;
                }
                
                // Save Score Card data into the database
                studentData.setFatherMother(h4Fields[0]);
                studentData.setGender(h4Fields[1]);
                studentData.setStandard(h4Fields[2]);
                studentData.setCentre(h4Fields[3].replaceFirst(": ", ""));
                
                // Save Score data into the database
                studentData.setAstronomySubject(h5Fields[0]);
                studentData.setBiologySubject(h5Fields[1]);
                studentData.setChemistrySubject(h5Fields[2]);
                studentData.setPhysicsSubject(h5Fields[3]);
                studentData.setAstronomyScore(h5Fields[4]);
                studentData.setBiologyScore(h5Fields[5]);
                studentData.setChemistryScore(h5Fields[6]);
                studentData.setPhysicsScore(h5Fields[7]);
                
                return new ResponseEntity<>(
                        studentDataMapper.toDto(studentDataRepository.save(studentData)),
                        HttpStatus.CREATED
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<String> getAllRollNo() {
        return studentDataRepository.getAllRollNo();
    }
    
}
