package com.threelabs.service;

import com.threelabs.error.exception.CustomErrorCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
@RequiredArgsConstructor
@Service
public class ProductMockService {

    private static final Map<String, Map<String, Object>> PRODUCT_MAP = new HashMap<>();

    static {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("productName", "샤또 마고 2018");
        product1.put("productImageUrl", "https://marketbang.kr/images/chateau-margaux-2018.jpg");
        product1.put("productDescription", "보르도 마고 지역의 프리미엄 레드 와인. 카베르네 소비뇽 중심의 블렌드로 깊은 풍미와 긴 여운이 특징입니다.");
        product1.put("importerName", "나라셀라");
        product1.put("originalPrice", 890000);
        PRODUCT_MAP.put("1", product1);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("productName", "오퍼스 원 2019");
        product2.put("productImageUrl", "https://marketbang.kr/images/opus-one-2019.jpg");
        product2.put("productDescription", "나파밸리 대표 프리미엄 와인. 로버트 몬다비와 바론 필립 드 로칠드의 합작 와인으로 우아하고 복합적인 풍미.");
        product2.put("importerName", "신세계 L&B");
        product2.put("originalPrice", 650000);
        PRODUCT_MAP.put("2", product2);

        Map<String, Object> product3 = new HashMap<>();
        product3.put("productName", "돔 페리뇽 2012");
        product3.put("productImageUrl", "https://marketbang.kr/images/dom-perignon-2012.jpg");
        product3.put("productDescription", "모엣 헤네시의 프레스티지 샴페인. 섬세한 거품과 복합적인 아로마가 특징인 빈티지 샴페인입니다.");
        product3.put("importerName", "모엣 헤네시 코리아");
        product3.put("originalPrice", 380000);
        PRODUCT_MAP.put("3", product3);

        Map<String, Object> product4 = new HashMap<>();
        product4.put("productName", "바롤로 몬포르티노 2015");
        product4.put("productImageUrl", "https://marketbang.kr/images/barolo-monfortino-2015.jpg");
        product4.put("productDescription", "피에몬테 네비올로 품종의 정수. 자코모 콘테르노의 플래그십 와인으로 장기 숙성 잠재력이 뛰어납니다.");
        product4.put("importerName", "와인투유코리아");
        product4.put("originalPrice", 720000);
        PRODUCT_MAP.put("4", product4);

        Map<String, Object> product5 = new HashMap<>();
        product5.put("productName", "클라우디 베이 소비뇽 블랑 2023");
        product5.put("productImageUrl", "https://marketbang.kr/images/cloudy-bay-sb-2023.jpg");
        product5.put("productDescription", "뉴질랜드 말보로 대표 화이트 와인. 열대 과일과 시트러스의 상큼한 아로마가 특징인 가성비 좋은 와인입니다.");
        product5.put("importerName", "모엣 헤네시 코리아");
        product5.put("originalPrice", 45000);
        PRODUCT_MAP.put("5", product5);
    }

    public Map<String, Object> fetchProductByUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new CustomErrorCodeException("상품 URL을 입력해주세요.", 1);
        }

        String productId = extractProductId(url);
        if (productId == null) {
            throw new CustomErrorCodeException("올바른 상품 URL 형식이 아닙니다. (예: https://marketbang.kr/detail/{id})", 1);
        }

        Map<String, Object> product = PRODUCT_MAP.get(productId);
        if (product == null) {
            throw new CustomErrorCodeException("상품을 찾을 수 없습니다.", 1);
        }

        return product;
    }

    private String extractProductId(String url) {
        String prefix = "https://marketbang.kr/detail/";
        if (!url.startsWith(prefix)) {
            return null;
        }
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
