package com.threelabs.controller.buyer;

import com.threelabs.dto.ResponseDto;
import com.threelabs.dto.buyer.request.ParticipationCancelRequestDto;
import com.threelabs.dto.buyer.request.ParticipationJoinRequestDto;
import com.threelabs.dto.buyer.response.MyParticipationResponseDto;
import com.threelabs.dto.buyer.response.ParticipationResponseDto;
import com.threelabs.service.ParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 - 참여")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/buyer/v1/participation")
public class BuyerParticipationController {

    private final ParticipationService participationService;

    @Operation(summary = "공동구매 참여")
    @PostMapping("/{groupBuyId}/join")
    public ResponseEntity<ResponseDto<ParticipationResponseDto>> joinGroupBuy(
            @PathVariable Long groupBuyId,
            @RequestBody ParticipationJoinRequestDto dto) {
        ParticipationResponseDto result = participationService.joinGroupBuy(groupBuyId, dto);
        return ResponseEntity.ok(new ResponseDto<>(0, "참여가 완료되었습니다.", result));
    }

    @Operation(summary = "공동구매 참여 취소")
    @PostMapping("/{groupBuyId}/cancel")
    public ResponseEntity<ResponseDto<Void>> cancelParticipation(
            @PathVariable Long groupBuyId,
            @RequestBody ParticipationCancelRequestDto dto) {
        participationService.cancelParticipation(groupBuyId, dto);
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
