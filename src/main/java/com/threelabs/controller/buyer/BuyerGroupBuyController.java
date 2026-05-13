package com.threelabs.controller.buyer;

import com.threelabs.dto.ResponseDto;
import com.threelabs.dto.buyer.response.GroupBuyBuyerDetailResponseDto;
import com.threelabs.dto.buyer.response.GroupBuyCardResponseDto;
import com.threelabs.dto.buyer.response.GroupBuyStatusResponseDto;
import com.threelabs.service.GroupBuyBuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 - 공동구매")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/buyer/v1/group-buy")
public class BuyerGroupBuyController {

    private final GroupBuyBuyerService groupBuyBuyerService;

    @Operation(summary = "진행중인 공동구매 목록 조회")
    @GetMapping
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
}
