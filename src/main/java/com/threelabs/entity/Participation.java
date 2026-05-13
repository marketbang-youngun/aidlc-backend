package com.threelabs.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.threelabs.enumration.ParticipationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "participation")
public class Participation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTICIPATION_NO")
    private Long participationNo;

    @Column(name = "GROUP_BUY_NO")
    private Long groupBuyNo;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_DISPLAY_NAME")
    private String userDisplayName;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "EXPECTED_AMOUNT")
    private Integer expectedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ParticipationStatus status;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "APPEND_DATE")
    private LocalDateTime appendDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "CANCELLED_AT")
    private LocalDateTime cancelledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_BUY_NO", insertable = false, updatable = false)
    private GroupBuy groupBuy;
}
