package com.threelabs.service;

import com.threelabs.dto.buyer.response.GroupBuyBuyerDetailResponseDto;
import com.threelabs.dto.buyer.response.GroupBuyCardResponseDto;
import com.threelabs.dto.buyer.response.GroupBuyStatusResponseDto;
import com.threelabs.dto.buyer.response.RecentParticipantDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
@RequiredArgsConstructor
@Service
public class GroupBuyBuyerService {

    private final GroupBuyRepository groupBuyRepository;
    private final ParticipationRepository participationRepository;

    @Transactional(readOnly = true)
    public List<GroupBuyCardResponseDto> getActiveGroupBuyList() {
        LocalDateTime now = LocalDateTime.now();
        List<GroupBuy> groupBuys = groupBuyRepository
                .findByStatusAndStartAtBeforeAndEndAtAfterAndUseYnOrderByAppendDateDesc(
                        GroupBuyStatus.RECRUITING, now, now, YnCode.Y);

        return groupBuys.stream()
                .map(g -> GroupBuyCardResponseDto.builder()
                        .id(g.getGroupBuyNo())
                        .title(g.getTitle())
                        .productName(g.getProductName())
                        .productImageUrl(g.getProductImageUrl())
                        .originalPrice(g.getOriginalPrice())
                        .groupbuyPrice(g.getGroupbuyPrice())
                        .targetQty(g.getTargetQuantity())
                        .currentQty(g.getCurrentQuantity())
                        .participantCount(g.getParticipantCount())
                        .endAt(g.getEndAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupBuyBuyerDetailResponseDto getGroupBuyDetail(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        List<Participation> recentParticipations = participationRepository
                .findByGroupBuyNoAndStatusOrderByAppendDateDesc(id, ParticipationStatus.ACTIVE);

        List<RecentParticipantDto> recentParticipants = recentParticipations.stream()
                .limit(5)
                .map(p -> RecentParticipantDto.builder()
                        .displayName(maskDisplayName(p.getUserDisplayName()))
                        .createdAt(p.getAppendDate())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyBuyerDetailResponseDto.builder()
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
                .recentParticipants(recentParticipants)
                .build();
    }

    @Transactional(readOnly = true)
    public GroupBuyStatusResponseDto getGroupBuyStatus(Long id) {
        GroupBuy groupBuy = groupBuyRepository.findById(id)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        List<Participation> recentParticipations = participationRepository
                .findByGroupBuyNoAndStatusOrderByAppendDateDesc(id, ParticipationStatus.ACTIVE);

        List<RecentParticipantDto> recentParticipants = recentParticipations.stream()
                .limit(3)
                .map(p -> RecentParticipantDto.builder()
                        .displayName(maskDisplayName(p.getUserDisplayName()))
                        .createdAt(p.getAppendDate())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyStatusResponseDto.builder()
                .currentQty(groupBuy.getCurrentQuantity())
                .participantCount(groupBuy.getParticipantCount())
                .status(groupBuy.getStatus())
                .recentParticipants(recentParticipants)
                .build();
    }

    private String maskDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) return "**";
        return displayName.charAt(0) + "**";
    }
}
