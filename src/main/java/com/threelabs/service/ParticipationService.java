package com.threelabs.service;

import com.threelabs.dto.buyer.request.ParticipationCancelRequestDto;
import com.threelabs.dto.buyer.request.ParticipationJoinRequestDto;
import com.threelabs.dto.buyer.response.MyParticipationResponseDto;
import com.threelabs.dto.buyer.response.ParticipationResponseDto;
import com.threelabs.entity.GroupBuy;
import com.threelabs.entity.Participation;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.enumration.ParticipationStatus;
import com.threelabs.error.exception.CustomErrorCodeException;
import com.threelabs.repository.GroupBuyRepository;
import com.threelabs.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CommonsLog
@RequiredArgsConstructor
@Service
public class ParticipationService {

    private final GroupBuyRepository groupBuyRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    public ParticipationResponseDto joinGroupBuy(Long groupBuyId, ParticipationJoinRequestDto dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new CustomErrorCodeException("이메일을 입력해주세요.", 1);
        }
        if (dto.getDisplayName() == null || dto.getDisplayName().isBlank()) {
            throw new CustomErrorCodeException("표시 이름을 입력해주세요.", 1);
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new CustomErrorCodeException("수량을 확인해주세요.", 1);
        }

        GroupBuy groupBuy = groupBuyRepository.findByIdForUpdate(groupBuyId)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.RECRUITING) {
            throw new CustomErrorCodeException("모집중인 공동구매만 참여할 수 있습니다.", 1);
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(groupBuy.getStartAt()) || now.isAfter(groupBuy.getEndAt())) {
            throw new CustomErrorCodeException("참여 가능한 기간이 아닙니다.", 1);
        }

        Optional<Participation> existingParticipation = participationRepository
                .findByGroupBuyNoAndUserEmailAndStatus(groupBuyId, dto.getEmail(), ParticipationStatus.ACTIVE);
        if (existingParticipation.isPresent()) {
            throw new CustomErrorCodeException("이미 참여한 공동구매입니다.", 1);
        }

        if (dto.getQuantity() < groupBuy.getMinPerUser()) {
            throw new CustomErrorCodeException("최소 주문 수량은 " + groupBuy.getMinPerUser() + "개입니다.", 1);
        }
        if (groupBuy.getMaxPerUser() != null && dto.getQuantity() > groupBuy.getMaxPerUser()) {
            throw new CustomErrorCodeException("최대 주문 수량은 " + groupBuy.getMaxPerUser() + "개입니다.", 1);
        }

        if (groupBuy.getMaxQuantity() != null) {
            int newTotal = groupBuy.getCurrentQuantity() + dto.getQuantity();
            if (newTotal > groupBuy.getMaxQuantity()) {
                throw new CustomErrorCodeException("최대 모집 수량을 초과할 수 없습니다.", 1);
            }
        }

        Integer expectedAmount = dto.getQuantity() * groupBuy.getGroupbuyPrice();

        Participation participation = new Participation();
        participation.setGroupBuyNo(groupBuyId);
        participation.setUserEmail(dto.getEmail());
        participation.setUserDisplayName(dto.getDisplayName());
        participation.setQuantity(dto.getQuantity());
        participation.setExpectedAmount(expectedAmount);
        participation.setStatus(ParticipationStatus.ACTIVE);
        participation.setAppendDate(LocalDateTime.now());

        Participation saved = participationRepository.save(participation);

        groupBuy.setCurrentQuantity(groupBuy.getCurrentQuantity() + dto.getQuantity());
        groupBuy.setParticipantCount(groupBuy.getParticipantCount() + 1);
        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);

        return ParticipationResponseDto.builder()
                .participationNo(saved.getParticipationNo())
                .groupBuyNo(saved.getGroupBuyNo())
                .userEmail(saved.getUserEmail())
                .userDisplayName(saved.getUserDisplayName())
                .quantity(saved.getQuantity())
                .expectedAmount(saved.getExpectedAmount())
                .status(saved.getStatus())
                .createdAt(saved.getAppendDate())
                .build();
    }

    @Transactional
    public void cancelParticipation(Long groupBuyId, ParticipationCancelRequestDto dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new CustomErrorCodeException("이메일을 입력해주세요.", 1);
        }

        Participation participation = participationRepository
                .findByGroupBuyNoAndUserEmailAndStatus(groupBuyId, dto.getEmail(), ParticipationStatus.ACTIVE)
                .orElseThrow(() -> new CustomErrorCodeException("참여 내역을 찾을 수 없습니다.", 1));

        GroupBuy groupBuy = groupBuyRepository.findByIdForUpdate(groupBuyId)
                .orElseThrow(() -> new CustomErrorCodeException("공동구매를 찾을 수 없습니다.", 1));

        if (groupBuy.getStatus() != GroupBuyStatus.RECRUITING) {
            throw new CustomErrorCodeException("모집중인 공동구매만 참여 취소할 수 있습니다.", 1);
        }

        participation.setStatus(ParticipationStatus.CANCELLED);
        participation.setCancelledAt(LocalDateTime.now());
        participationRepository.save(participation);

        groupBuy.setCurrentQuantity(groupBuy.getCurrentQuantity() - participation.getQuantity());
        groupBuy.setParticipantCount(groupBuy.getParticipantCount() - 1);
        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);
    }

    @Transactional(readOnly = true)
    public List<MyParticipationResponseDto> getMyParticipations(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomErrorCodeException("이메일을 입력해주세요.", 1);
        }

        List<ParticipationStatus> statuses = Arrays.asList(
                ParticipationStatus.ACTIVE, ParticipationStatus.CONFIRMED);

        List<Participation> participations = participationRepository
                .findByUserEmailAndStatusInOrderByAppendDateDesc(email, statuses);

        return participations.stream()
                .map(p -> {
                    GroupBuy groupBuy = groupBuyRepository.findById(p.getGroupBuyNo()).orElse(null);
                    String title = groupBuy != null ? groupBuy.getTitle() : "";
                    return MyParticipationResponseDto.builder()
                            .groupBuyTitle(title)
                            .quantity(p.getQuantity())
                            .expectedAmount(p.getExpectedAmount())
                            .status(p.getStatus())
                            .createdAt(p.getAppendDate())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
