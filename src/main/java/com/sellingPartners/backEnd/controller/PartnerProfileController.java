package com.sellingPartners.backEnd.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sellingPartners.backEnd.dto.PartnerProfileRequest;
import com.sellingPartners.backEnd.dto.PartnerProfileResponse;
import com.sellingPartners.backEnd.dto.PartnerProfileUpdateRequest;
import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.entity.PartnerProfileEntity;
import com.sellingPartners.backEnd.service.PartnerProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partner-profiles")
public class PartnerProfileController {
	
    private final PartnerProfileService partnerProfileService;
    
   
    @Secured("ROLE_USER")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProfile(
            @RequestParam("title") String title,
            @RequestParam("introduction") String introduction,
            @RequestParam("summary") String summary,
            @RequestParam("category") Category category,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("brochure") MultipartFile brochure
            
    ) throws Exception {
        PartnerProfileRequest dto = new PartnerProfileRequest();
        dto.setTitle(title);
        dto.setIntroduction(introduction);
        dto.setSummary(summary);
        dto.setCategory(category);

        PartnerProfileEntity created = partnerProfileService.createProfile(dto, thumbnail, brochure);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("")
    public ResponseEntity<Page<PartnerProfileResponse>> getProfiles(@PageableDefault(size = 18) Pageable pageable) {
        Page<PartnerProfileResponse> pageDtos = partnerProfileService.getProfiles(pageable);
        return ResponseEntity.ok(pageDtos);
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PartnerProfileResponse> updateProfile(
            @PathVariable("id") Long id,
            @ModelAttribute PartnerProfileUpdateRequest request) throws Exception {
        PartnerProfileResponse updated = partnerProfileService.updateProfile(id, request);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id) {
        partnerProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PartnerProfileResponse> getProfileDetail(@PathVariable("id") Long id) {
        PartnerProfileResponse response = partnerProfileService.getProfileDetail(id);
        return ResponseEntity.ok(response);
    }



}
