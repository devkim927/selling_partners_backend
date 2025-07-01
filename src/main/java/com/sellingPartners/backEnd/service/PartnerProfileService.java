package com.sellingPartners.backEnd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sellingPartners.backEnd.dto.PartnerProfileRequest;
import com.sellingPartners.backEnd.dto.PartnerProfileResponse;
import com.sellingPartners.backEnd.dto.PartnerProfileUpdateRequest;
import com.sellingPartners.backEnd.entity.PartnerProfileEntity;
import com.sellingPartners.backEnd.repository.PartnerProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PartnerProfileService {
	
	
	private final PartnerProfileRepository partnerProfileRepository;
    private final FileStorageService fileService;
    private final UserService userService;
    
    // 생성
    
    public PartnerProfileEntity createProfile(PartnerProfileRequest dto,
            MultipartFile thumbnail,
            MultipartFile brochure) throws Exception {

	String thumbnailUrl = fileService.storeFile(thumbnail,"partnerProfiles");
	String brochureUrl = fileService.storeFile(brochure,"partnerProfiles");
	
	PartnerProfileEntity profile = PartnerProfileEntity.builder()
	.title(dto.getTitle())
	.introduction(dto.getIntroduction())
	.summary(dto.getSummary())
	.category(dto.getCategory())
	.thumbnailUrl(thumbnailUrl)
	.brochureUrl(brochureUrl)
	.user(userService.getCurrentUser())
	.build();
	
	return partnerProfileRepository.save(profile);
	}
    
    public Page<PartnerProfileResponse> getProfiles(Pageable pageable) {
        Page<PartnerProfileEntity> pageEntities = partnerProfileRepository.findAll(pageable);

        Page<PartnerProfileResponse> pageDtos = pageEntities.map(entity -> new PartnerProfileResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getSummary(),
            entity.getCategory(),
            entity.getBrochureUrl(),
            entity.getIntroduction(),
            entity.getThumbnailUrl(),
            entity.getCreateAt().toString(),
            entity.getUser().getName()
        ));

        return pageDtos;
    }
    
    // 수정
    
    @Transactional
    public PartnerProfileResponse updateProfile(Long id, PartnerProfileUpdateRequest request) throws Exception {
        PartnerProfileEntity entity = partnerProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("프로필 없음"));

        // 필드 업데이트 (null 체크해서 선택적 업데이트 가능)
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getSummary() != null) {
            entity.setSummary(request.getSummary());
        }
        if (request.getIntroduction() != null) {
            entity.setIntroduction(request.getIntroduction());
        }
        if (request.getCategory() != null) {
            entity.setCategory(request.getCategory());
        }

        // 파일 처리 (예: 브로셔 파일, 썸네일 파일)
        if (request.getBrochureFile() != null && !request.getBrochureFile().isEmpty()) {
            // 파일 저장 로직 호출 후 URL 업데이트
            String brochureUrl = fileService.storeFile(request.getBrochureFile(),"partnerProfiles");
            entity.setBrochureUrl(brochureUrl);
        }
        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            String thumbnailUrl = fileService.storeFile(request.getThumbnailFile(),"partnerProfiles");
            entity.setThumbnailUrl(thumbnailUrl);
        }

        partnerProfileRepository.save(entity);

        return new PartnerProfileResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getSummary(),
            entity.getCategory(),
            entity.getBrochureUrl(),
            entity.getIntroduction(),
            entity.getThumbnailUrl(),
            entity.getCreateAt().toString(),
            entity.getUser().getName()
        );
    }
    
    // 삭제
    
    @Transactional
    public void deleteProfile(Long id) {
        PartnerProfileEntity entity = partnerProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("프로필 없음"));
        partnerProfileRepository.delete(entity);
    }
    
    // 상세
    
    public PartnerProfileResponse getProfileDetail(Long id) {
        PartnerProfileEntity entity = partnerProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다. id=" + id));

        PartnerProfileResponse response = new PartnerProfileResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setSummary(entity.getSummary());
        response.setCategory(entity.getCategory());
        response.setBrochureUrl(entity.getBrochureUrl());
        response.setIntroduction(entity.getIntroduction());
        response.setThumbnailUrl(entity.getThumbnailUrl());
        response.setCreateAt(entity.getCreateAt().toString());
        response.setName(entity.getUser().getName());

        return response;
    }

}
