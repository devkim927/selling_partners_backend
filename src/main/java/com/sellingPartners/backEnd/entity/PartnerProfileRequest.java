package com.sellingPartners.backEnd.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProfileRequest {

	private String title;
	private String introduction;
	private String summary;
	private Category category;
	
}
