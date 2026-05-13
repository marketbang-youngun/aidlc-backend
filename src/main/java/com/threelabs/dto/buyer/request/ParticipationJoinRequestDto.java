package com.threelabs.dto.buyer.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParticipationJoinRequestDto {

    private String email;
    private String displayName;
    private Integer quantity;
}
