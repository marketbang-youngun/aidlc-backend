package com.threelabs.controller.common;

import com.threelabs.dto.ResponseDto;
import com.threelabs.service.ProductMockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "공통 - 상품")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/common/v1/product")
public class CommonProductController {

    private final ProductMockService productMockService;

    @Operation(summary = "상품 정보 조회 (Mock)")
    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto<Map<String, Object>>> fetchProduct(@RequestParam String url) {
        Map<String, Object> product = productMockService.fetchProductByUrl(url);
        return ResponseEntity.ok(new ResponseDto<>(0, "", product));
    }
}
