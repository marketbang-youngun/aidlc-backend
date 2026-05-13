package com.threelabs.dto.buyer.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GroupBuyCardResponseDto {

    private Long id;
    private String title;
    private String productName;
    private String productImageUrl;
    private Integer originalPrice;
    private Integer groupbuyPrice;
    private Integer targetQty;
    private Integer currentQty;
    private Integer participantCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endAt;
}
