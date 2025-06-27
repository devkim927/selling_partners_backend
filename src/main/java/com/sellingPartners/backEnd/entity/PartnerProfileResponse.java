package com.sellingPartners.backEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProfileResponse {

    private Long id;  
    private String title;
    private String summary;
    private Category category;
    private String brochureUrl;
    private String introduction;
    private String thumbnailUrl;
    private String createAt; 
    private String name;
}
