package com.threelabs.controller.buyer;

import com.threelabs.dto.ResponseDto;
import com.threelabs.dto.buyer.request.ParticipationCancelRequestDto;
import com.threelabs.dto.buyer.request.ParticipationJoinRequestDto;
import com.threelabs.dto.buyer.response.*;
import com.threelabs.service.GroupBuyBuyerService;
import com.threelabs.service.ParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 - 공동구매")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/buyer/v1/public/groupbuy")
public class BuyerGroupBuyController {

    private final GroupBuyBuyerService groupBuyBuyerService;
    private final ParticipationService participationService;

    @Operation(summary = "진행중인 공동구매 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<GroupBuyCardResponseDto>>> getActiveGroupBuyList() {
        List<GroupBuyCardResponseDto> list = groupBuyBuyerService.getActiveGroupBuyList();
        return ResponseEntity.ok(new ResponseDto<>(0, "", list));
    }

    @Operation(summary = "공동구매 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GroupBuyBuyerDetailResponseDto>> getGroupBuyDetail(@PathVariable Long id) {
        GroupBuyBuyerDetailResponseDto detail = groupBuyBuyerService.getGroupBuyDetail(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "", detail));
    }

    @Operation(summary = "공동구매 현황 조회 (폴링)")
    @GetMapping("/{id}/status")
    public ResponseEntity<ResponseDto<GroupBuyStatusResponseDto>> getGroupBuyStatus(@PathVariable Long id) {
        GroupBuyStatusResponseDto status = groupBuyBuyerService.getGroupBuyStatus(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "", status));
    }

    @Operation(summary = "공동구매 참여")
    @PostMapping("/{id}/join")
    public ResponseEntity<ResponseDto<ParticipationResponseDto>> joinGroupBuy(
            @PathVariable Long id,
            @RequestBody ParticipationJoinRequestDto dto) {
        ParticipationResponseDto result = participationService.joinGroupBuy(id, dto);
        return ResponseEntity.ok(new ResponseDto<>(0, "참여가 완료되었습니다.", result));
    }

    @Operation(summary = "공동구매 참여 취소")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ResponseDto<Void>> cancelParticipation(
            @PathVariable Long id,
            @RequestBody ParticipationCancelRequestDto dto) {
        participationService.cancelParticipation(id, dto);
        return ResponseEntity.ok(new ResponseDto<>(0, "참여가 취소되었습니다.", null));
    }

    @Operation(summary = "내 참여 내역 조회")
    @GetMapping("/my")
    public ResponseEntity<ResponseDto<List<MyParticipationResponseDto>>> getMyParticipations(
            @RequestParam String email) {
        List<MyParticipationResponseDto> list = participationService.getMyParticipations(email);
        return ResponseEntity.ok(new ResponseDto<>(0, "", list));
    }
}
