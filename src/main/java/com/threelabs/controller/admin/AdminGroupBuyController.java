package com.threelabs.controller.admin;

import com.threelabs.dto.ResponseDto;
import com.threelabs.dto.admin.request.GroupBuyCreateRequestDto;
import com.threelabs.dto.admin.request.GroupBuyUpdateRequestDto;
import com.threelabs.dto.admin.response.GroupBuyDetailResponseDto;
import com.threelabs.dto.admin.response.GroupBuyListResponseDto;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.service.GroupBuyAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "어드민 - 공동구매")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/v1/group-buy")
public class AdminGroupBuyController {

    private final GroupBuyAdminService groupBuyAdminService;

    @Operation(summary = "공동구매 생성")
    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createGroupBuy(@RequestBody GroupBuyCreateRequestDto dto) {
        Long id = groupBuyAdminService.createGroupBuy(dto);
        return ResponseEntity.ok(new ResponseDto<>(0, "공동구매가 생성되었습니다.", id));
    }

    @Operation(summary = "공동구매 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Page<GroupBuyListResponseDto>>> getGroupBuyList(
            @RequestParam(required = false) GroupBuyStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GroupBuyListResponseDto> list = groupBuyAdminService.getGroupBuyList(status, pageable);
        return ResponseEntity.ok(new ResponseDto<>(0, "", list));
    }

    @Operation(summary = "공동구매 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GroupBuyDetailResponseDto>> getGroupBuyDetail(@PathVariable Long id) {
        GroupBuyDetailResponseDto detail = groupBuyAdminService.getGroupBuyDetail(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "", detail));
    }

    @Operation(summary = "공동구매 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> updateGroupBuy(
            @PathVariable Long id, @RequestBody GroupBuyUpdateRequestDto dto) {
        groupBuyAdminService.updateGroupBuy(id, dto);
        return ResponseEntity.ok(new ResponseDto<>(0, "공동구매가 수정되었습니다.", null));
    }

    @Operation(summary = "공동구매 취소")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ResponseDto<Void>> cancelGroupBuy(@PathVariable Long id) {
        groupBuyAdminService.cancelGroupBuy(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "공동구매가 취소되었습니다.", null));
    }

    @Operation(summary = "공동구매 준비중 변경")
    @PatchMapping("/{id}/prepare")
    public ResponseEntity<ResponseDto<Void>> prepareGroupBuy(@PathVariable Long id) {
        groupBuyAdminService.prepareGroupBuy(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "공동구매가 준비중으로 변경되었습니다.", null));
    }

    @Operation(summary = "공동구매 완료 처리")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ResponseDto<Void>> completeGroupBuy(@PathVariable Long id) {
        groupBuyAdminService.completeGroupBuy(id);
        return ResponseEntity.ok(new ResponseDto<>(0, "공동구매가 완료되었습니다.", null));
    }
}
