package com.threelabs.dto.buyer.response;

import com.threelabs.enumration.GroupBuyStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GroupBuyStatusResponseDto {

    private Integer currentQty;
    private Integer participantCount;
    private GroupBuyStatus status;
    private List<RecentParticipantDto> recentParticipants;
}
