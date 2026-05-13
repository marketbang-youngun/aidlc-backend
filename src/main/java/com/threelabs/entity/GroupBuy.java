package com.threelabs.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.enumration.code.YnCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "group_buy")
public class GroupBuy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_BUY_NO")
    private Long groupBuyNo;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PRODUCT_URL")
    private String productUrl;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_IMAGE_URL")
    private String productImageUrl;

    @Column(name = "PRODUCT_DESCRIPTION", columnDefinition = "TEXT")
    private String productDescription;

    @Column(name = "IMPORTER_NAME")
    private String importerName;

    @Column(name = "ORIGINAL_PRICE")
    private Integer originalPrice;

    @Column(name = "GROUPBUY_PRICE")
    private Integer groupbuyPrice;

    @Column(name = "TARGET_QUANTITY")
    private Integer targetQuantity;

    @Column(name = "MAX_QUANTITY")
    private Integer maxQuantity;

    @Column(name = "MIN_PER_USER")
    private Integer minPerUser;

    @Column(name = "MAX_PER_USER")
    private Integer maxPerUser;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "START_AT")
    private LocalDateTime startAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "END_AT")
    private LocalDateTime endAt;

    @Column(name = "DELIVERY_DATE")
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private GroupBuyStatus status;

    @Column(name = "CURRENT_QUANTITY")
    private Integer currentQuantity;

    @Column(name = "PARTICIPANT_COUNT")
    private Integer participantCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "USE_YN")
    private YnCode useYn;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "APPEND_DATE")
    private LocalDateTime appendDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
