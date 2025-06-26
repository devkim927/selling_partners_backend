package com.sellingPartners.backEnd.dto;

import com.sellingPartners.backEnd.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectCreateRequest {
    private String title;
    private String description;
    private String summary;
    private MultipartFile thumbnail;
    private MultipartFile brochure;
    private Category category;
    private LocalDate deadline;
}
