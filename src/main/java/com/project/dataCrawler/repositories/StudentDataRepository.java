package com.project.dataCrawler.repositories;

import com.project.dataCrawler.domain.entities.StudentDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDataRepository extends JpaRepository<StudentDataEntity, Long> {
    
    @Query("SELECT s.rollNo FROM StudentDataEntity s")
    List<String> getAllRollNo();
    
}
