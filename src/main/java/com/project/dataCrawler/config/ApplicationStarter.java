package com.project.dataCrawler.config;

import com.project.dataCrawler.services.DataCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Component
public class ApplicationStarter {
    
    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private DataCrawlerService dataCrawlerService;
    
    private final List<String> rollNoList = new ArrayList<>();
    private final List<String> dobList = new ArrayList<>();
    
    public void populateData() throws IOException {
        // Extract and Populate rollNoList
        rollNoList.addAll(extractRollNoFromAllPdfFiles());
        
        log.info("Total Roll Numbers: {}", rollNoList.size());
        
        // Fetch and Remove all the rollNo which exists in the database
        dataCrawlerService.getAllRollNo().forEach(rollNoList::remove);
        
        log.info("Remaining Roll Numbers: {}", rollNoList.size());
        
        // Input date range
        String startDate = "01/07/2005";
        String endDate = "31/12/2011";
        
        // Populate dobList from startDate to endDate
        dobList.addAll(getDatesBetween(startDate, endDate));
        
    }
    
    private List<String> getDatesBetween(String startDate, String endDate) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Parse the start and end dates
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        
        // Initialize the Set to store the dates
        List<String> dates = new ArrayList<>();
        
        // Iterate from start date to end date (inclusive)
        while (!start.isAfter(end)) {
            // Format the date and add to the Set
            dates.add(start.format(formatter));
            // Move to the next day
            start = start.plusDays(1);
        }
        
        return dates;
    }
    
    private Set<String> extractRollNoFromAllPdfFiles() throws IOException {
        Set<String> result = new HashSet<>();
        
        // All PDF Files
        List<File> allPdfFiles = getAllPdfFiles();
        
        log.info("Total PDF Files found: {}", allPdfFiles.size());
        
        for (File pdfFile : allPdfFiles) {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfText = pdfTextStripper.getText(document);
            document.close();
            
            result.addAll(
                    Arrays.stream(pdfText.split("\n"))
                            .map(line -> line.split("\\s+"))                 // Split each line into words
                            .filter(words -> words.length > 0)                     // Ensure line has enough words
                            .flatMap(words -> Arrays.stream(words)                 // Flatten the words stream
                                                      .filter(word -> word.matches("[A-Z]{2}[0-9]{9}")) // Filter words by the pattern
                            ).toList()                                             // Collect into a list
            );
        }
        
        return result;
    }
    
    private List<File> getAllPdfFiles() {
        List<File> pdfFiles = new ArrayList<>();
        
        // PDF folder path
        String pdfFolderPath = appConfig.getPdfFolderPath();
        
        File pdfFolder = new File(pdfFolderPath);
        
        if (pdfFolder.exists() && pdfFolder.isDirectory()) {
            // Filter and list all PDF files in the folder
            File[] files = pdfFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
            
            if (files != null) pdfFiles.addAll(Arrays.asList(files));
        } else {
            throw new RuntimeException("The folder path is invalid or does not exist: " + pdfFolderPath);
        }
        
        return pdfFiles;
    }
    
    public void startThreads() {
        // Create a ForkJoinPool with a custom thread count
        ForkJoinPool customThreadPool = new ForkJoinPool(20);
        
        customThreadPool.submit(() -> {
            // Process every rollNo with all dobs
            rollNoList.parallelStream().forEach(rollNo -> {
                try {
                    // Process every dob with the rollNo and find a match
                    boolean fetch = dobList.stream().map(
                            dob -> dataCrawlerService.fetchAndSaveData(rollNo, dob).getStatusCode().value()
                    ).anyMatch(code -> code == 201);
                    
                    if (fetch)
                        log.info("Data fetched and saved successfully for {}", rollNo);
                    else
                        log.info("Data not found for {}", rollNo);
                } catch (Exception e) {
                    log.error("Error processing RollNo: {}", rollNo, e);
                }
            });
        }).join();
    }
}
