package com.sellingPartners.backEnd.dto;

import org.springframework.web.multipart.MultipartFile;

import com.sellingPartners.backEnd.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProfileUpdateRequest {
	
    private String title;
    private String introduction;
    private String summary;
    private Category category;
    
    private MultipartFile brochureFile;  
    private MultipartFile thumbnailFile; 

}
