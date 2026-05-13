package com.threelabs.service;

import com.threelabs.error.exception.CustomErrorCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
@RequiredArgsConstructor
@Service
public class ProductMockService {

    private final RestTemplate restTemplate;

    private static final String API_PREFIX = "https://api.marketbang.kr/api/buyer/v1/goods/";

    public Map<String, Object> fetchProductByUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new CustomErrorCodeException("상품 URL을 입력해주세요.", 1);
        }

        String productId = extractProductId(url);
        if (productId == null) {
            throw new CustomErrorCodeException("올바른 상품 URL 형식이 아닙니다. (예: https://api.marketbang.kr/api/buyer/v1/goods/{id})", 1);
        }

        try {
            String apiUrl = API_PREFIX + productId;
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response == null || !Integer.valueOf(0).equals(response.get("code"))) {
                throw new CustomErrorCodeException("상품을 찾을 수 없습니다.", 1);
            }

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data == null) {
                throw new CustomErrorCodeException("상품을 찾을 수 없습니다.", 1);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("productName", data.get("wineGoodNameKo"));
            result.put("productDescription", buildDescription(data));
            result.put("importerName", data.get("companyName"));
            result.put("originalPrice", data.get("vintageSupplyPrice"));

            Map<String, Object> fileInfo = (Map<String, Object>) data.get("wineGoodFile");
            if (fileInfo != null) {
                result.put("productImageUrl", fileInfo.get("fileLink"));
            }

            return result;
        } catch (CustomErrorCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error("상품 정보 조회 실패: " + e.getMessage(), e);
            throw new CustomErrorCodeException("상품 정보를 가져오는데 실패했습니다.", 1);
        }
    }

    private String buildDescription(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        String kindName = (String) data.get("wineGoodKindCdName");
        String placeName = (String) data.get("wineGoodPlaceCd0Name");
        if (kindName != null) sb.append(kindName);
        if (placeName != null) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(placeName);
        }
        return sb.toString();
    }

    private String extractProductId(String url) {
        String[] prefixes = {
                "https://marketbang.kr/detail/",
                "https://api.marketbang.kr/api/buyer/v1/goods/"
        };
        for (String prefix : prefixes) {
            if (url.startsWith(prefix)) {
                String id = url.substring(prefix.length()).trim();
                if (id.isEmpty()) {
                    return null;
                }
                if (id.contains("/")) {
                    id = id.substring(0, id.indexOf("/"));
                }
                if (id.contains("?")) {
                    id = id.substring(0, id.indexOf("?"));
                }
                return id;
            }
        }
        return null;
    }
}
