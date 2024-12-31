package com.project.dataCrawler.mappers;

import com.project.dataCrawler.domain.dtos.StudentDataDto;
import com.project.dataCrawler.domain.entities.StudentDataEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentDataMapper {

    public StudentDataDto toDto(StudentDataEntity studentDataEntity) {
        return new StudentDataDto(
                studentDataEntity.getId(),
                studentDataEntity.getRollNo(),
                studentDataEntity.getEnrollmentNo(),
                studentDataEntity.getDob(),
                studentDataEntity.getName(),
                studentDataEntity.getMobileNo(),
                studentDataEntity.getEmailId(),
                studentDataEntity.getFatherMother(),
                studentDataEntity.getGender(),
                studentDataEntity.getStandard(),
                studentDataEntity.getCentre(),
                studentDataEntity.getAstronomySubject(),
                studentDataEntity.getBiologySubject(),
                studentDataEntity.getChemistrySubject(),
                studentDataEntity.getPhysicsSubject(),
                studentDataEntity.getAstronomyScore(),
                studentDataEntity.getBiologyScore(),
                studentDataEntity.getChemistryScore(),
                studentDataEntity.getPhysicsScore()
        );
    }

}
