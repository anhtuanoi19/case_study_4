package com.example.case_study_4.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDto {

    private String studentName;
    private String studentEmail;
    private String courseTitles;

}
