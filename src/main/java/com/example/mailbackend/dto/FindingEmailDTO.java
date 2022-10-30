package com.example.mailbackend.dto;

import com.example.mailbackend.constant.AppConstants;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class FindingEmailDTO {
    private String text;

    int pageNumber= Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER);
    int pageSize= Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);

    String sortBy;
    Sort.Direction sortDir;
}
