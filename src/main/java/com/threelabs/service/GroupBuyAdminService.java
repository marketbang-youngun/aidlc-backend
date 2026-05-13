package com.threelabs.service;

import com.threelabs.dto.admin.request.GroupBuyCreateRequestDto;
import com.threelabs.dto.admin.request.GroupBuyUpdateRequestDto;
import com.threelabs.dto.admin.response.GroupBuyDetailResponseDto;
import com.threelabs.dto.admin.response.GroupBuyListResponseDto;
import com.threelabs.dto.admin.response.ParticipantResponseDto;
import com.threelabs.entity.GroupBuy;
import com.threelabs.entity.Participation;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.enumration.ParticipationStatus;
import com.threelabs.enumration.code.YnCode;
import com.threelabs.error.exception.CustomErrorCodeException;
import com.threelabs.repository.GroupBuyRepository;
import com.threelabs.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
@RequiredArgsConstructor
@Service
public class GroupBuyAdminService {

    private final GroupBuyRepository groupBuyRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    public Long createGroupBuy(GroupBuyCreateRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new CustomErrorCodeException("제목을 입력해주세요.", 1);
        }
        if (dto.getProductName() == null || dto.getProductName().isBlank()) {
            throw new CustomErrorCodeException("상품명을 입력해주세요.", 1);
        }
        if (dto.getTargetQuantity() == null || dto.getTargetQuantity() <= 0) {
            throw new CustomErrorCodeException("목표 수량을 확인해주세요.", 1);
        }
        if (dto.getGroupbuyPrice() == null || dto.getGroupbuyPrice() <= 0) {
            throw new CustomErrorCodeException("공동구매 가격을 확인해주세요.", 1);
        }
        if (dto.getStartAt() == null || dto.getEndAt() == null) {
            throw new CustomErrorCodeException("시작일과 종료일을 입력해주세요.", 1);
        }
        if (dto.getEndAt().isBefore(dto.getStartAt())) {
            throw new CustomErrorCodeException("종료일은 시작일 이후여야 합니다.", 1);
        }

        GroupBuy groupBuy = new GroupBuy();
        groupBuy.setTitle(dto.getTitle());
        groupBuy.setProductUrl(dto.getProductUrl());
        groupBuy.setProductName(dto.getProductName());
        groupBuy.setProductImageUrl(dto.getProductImageUrl());
        groupBuy.setProductDescription(dto.getProductDescription());
        groupBuy.setImporterName(dto.getImporterName());
        groupBuy.setOriginalPrice(dto.getOriginalPrice());
        groupBuy.setGroupbuyPrice(dto.getGroupbuyPrice());
        groupBuy.setTargetQuantity(dto.getTargetQuantity());
        groupBuy.setMaxQuantity(dto.getMaxQuantity());
        groupBuy.setMinPerUser(dto.getMinPerUser() != null ? dto.getMinPerUser() : 1);
        groupBuy.setMaxPerUser(dto.getMaxPerUser());
        groupBuy.setStartAt(dto.getStartAt());
        groupBuy.setEndAt(dto.getEndAt());
        groupBuy.setDeliveryDate(dto.getDeliveryDate());
        groupBuy.setStatus(GroupBuyStatus.RECRUITING);
        groupBuy.setCurrentQuantity(0);
        groupBuy.setParticipantCount(0);
        groupBuy.setUseYn(YnCode.Y);
        groupBuy.setAppendDate(LocalDateTime.now());
        groupBuy.setUpdateDate(LocalDateTime.now());

        GroupBuy saved = groupBuyRepository.save(groupBuy);
        return saved.getGroupBuyNo();
    }

    @Transactional(readOnly = true)
    public Page<GroupBuyListResponseDto> getGroupBuyList(GroupBuyStatus status, Pageable pageable) {
        Page<GroupBuy> page;
        if (status != null) {
            page = groupBuyRepository.findByStatusAndUseYnOrderByAppendDateDesc(status, YnCode.Y, pageable);
        } else {
            page = groupBuyRepository.findByUseYnOrderByAppendDateDesc(YnCode.Y, pageable);
        }

        return page.map(g -> GroupBuyListResponseDto.builder()
                .id(g.getGroupBuyNo())
                .title(g.getTitle())
                .status(g.getStatus())
                .currentQty(g.getCurrentQuantity())
                .targetQty(g.getTargetQuantity())
                .endAt(g.getEndAt())
                .productName(g.getProductName())
                .productImageUrl(g.getProductImageUrl())
                .build());
    }

    @Transactional(readOnly = true)
    public GroupBuyDetailResponseDto getGroupBuyDetail(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        List<Participation> participations = participationRepository
                .findByGroupBuyNoAndStatusOrderByAppendDateDesc(id, ParticipationStatus.ACTIVE);

        List<ParticipantResponseDto> participantDtos = participations.stream()
                .map(p -> ParticipantResponseDto.builder()
                        .email(maskEmail(p.getUserEmail()))
                        .displayName(p.getUserDisplayName())
                        .quantity(p.getQuantity())
                        .amount(p.getExpectedAmount())
                        .status(p.getStatus())
                        .createdAt(p.getAppendDate())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyDetailResponseDto.builder()
                .id(groupBuy.getGroupBuyNo())
                .title(groupBuy.getTitle())
                .productUrl(groupBuy.getProductUrl())
                .productName(groupBuy.getProductName())
                .productImageUrl(groupBuy.getProductImageUrl())
                .productDescription(groupBuy.getProductDescription())
                .importerName(groupBuy.getImporterName())
                .originalPrice(groupBuy.getOriginalPrice())
                .groupbuyPrice(groupBuy.getGroupbuyPrice())
                .targetQuantity(groupBuy.getTargetQuantity())
                .maxQuantity(groupBuy.getMaxQuantity())
                .minPerUser(groupBuy.getMinPerUser())
                .maxPerUser(groupBuy.getMaxPerUser())
                .startAt(groupBuy.getStartAt())
                .endAt(groupBuy.getEndAt())
                .deliveryDate(groupBuy.getDeliveryDate())
                .status(groupBuy.getStatus())
                .currentQuantity(groupBuy.getCurrentQuantity())
                .participantCount(groupBuy.getParticipantCount())
                .appendDate(groupBuy.getAppendDate())
                .updateDate(groupBuy.getUpdateDate())
                .participants(participantDtos)
                .build();
    }

    @Transactional
    public void updateGroupBuy(Long id, GroupBuyUpdateRequestDto dto) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.RECRUITING) {
            throw new CustomErrorCodeException("모집중인 공동구매만 수정할 수 있습니다.", 1);
        }

        if (dto.getTitle() != null) groupBuy.setTitle(dto.getTitle());
        if (dto.getProductUrl() != null) groupBuy.setProductUrl(dto.getProductUrl());
        if (dto.getProductName() != null) groupBuy.setProductName(dto.getProductName());
        if (dto.getProductImageUrl() != null) groupBuy.setProductImageUrl(dto.getProductImageUrl());
        if (dto.getProductDescription() != null) groupBuy.setProductDescription(dto.getProductDescription());
        if (dto.getImporterName() != null) groupBuy.setImporterName(dto.getImporterName());
        if (dto.getOriginalPrice() != null) groupBuy.setOriginalPrice(dto.getOriginalPrice());
        if (dto.getGroupbuyPrice() != null) groupBuy.setGroupbuyPrice(dto.getGroupbuyPrice());
        if (dto.getTargetQuantity() != null) groupBuy.setTargetQuantity(dto.getTargetQuantity());
        if (dto.getMaxQuantity() != null) groupBuy.setMaxQuantity(dto.getMaxQuantity());
        if (dto.getMinPerUser() != null) groupBuy.setMinPerUser(dto.getMinPerUser());
        if (dto.getMaxPerUser() != null) groupBuy.setMaxPerUser(dto.getMaxPerUser());
        if (dto.getStartAt() != null) groupBuy.setStartAt(dto.getStartAt());
        if (dto.getEndAt() != null) groupBuy.setEndAt(dto.getEndAt());
        if (dto.getDeliveryDate() != null) groupBuy.setDeliveryDate(dto.getDeliveryDate());

        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);
    }

    @Transactional
    public void cancelGroupBuy(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.RECRUITING) {
            throw new CustomErrorCodeException("모집중인 공동구매만 취소할 수 있습니다.", 1);
        }

        groupBuy.setStatus(GroupBuyStatus.MANUAL_CANCELLED);
        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);

        participationRepository.updateStatusByGroupBuyNo(id, ParticipationStatus.CANCELLED);
    }

    @Transactional
    public void prepareGroupBuy(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.CONFIRMED) {
            throw new CustomErrorCodeException("확정된 공동구매만 준비중으로 변경할 수 있습니다.", 1);
        }

        groupBuy.setStatus(GroupBuyStatus.PREPARING);
        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);
    }

    @Transactional
    public void completeGroupBuy(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.PREPARING) {
            throw new CustomErrorCodeException("준비중인 공동구매만 완료할 수 있습니다.", 1);
        }

        groupBuy.setStatus(GroupBuyStatus.COMPLETED);
        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponseDto> getParticipants(Long groupBuyId) {
        groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        List<Participation> participations = participationRepository
                .findByGroupBuyNoAndStatusOrderByAppendDateDesc(groupBuyId, ParticipationStatus.ACTIVE);

        return participations.stream()
                .map(p -> ParticipantResponseDto.builder()
                        .email(maskEmail(p.getUserEmail()))
                        .displayName(p.getUserDisplayName())
                        .quantity(p.getQuantity())
                        .amount(p.getExpectedAmount())
                        .status(p.getStatus())
                        .createdAt(p.getAppendDate())
                        .build())
                .collect(Collectors.toList());
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String local = parts[0];
        if (local.length() <= 2) {
            return local.charAt(0) + "**@" + parts[1];
        }
        return local.charAt(0) + "**@" + parts[1];
    }
}
