package com.threelabs.dto.buyer.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.threelabs.enumration.GroupBuyStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class GroupBuyBuyerDetailResponseDto {

    private Long id;
    private String title;
    private String productUrl;
    private String productName;
    private String productImageUrl;
    private String productDescription;
    private String importerName;
    private Integer originalPrice;
    private Integer groupbuyPrice;
    private Integer targetQuantity;
    private Integer maxQuantity;
    private Integer minPerUser;
    private Integer maxPerUser;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endAt;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deliveryDate;

    private GroupBuyStatus status;
    private Integer currentQuantity;
    private Integer participantCount;

    private List<RecentParticipantDto> recentParticipants;
}
