package com.threelabs.schedule;

import com.threelabs.entity.GroupBuy;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.enumration.ParticipationStatus;
import com.threelabs.enumration.code.YnCode;
import com.threelabs.repository.GroupBuyRepository;
import com.threelabs.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@CommonsLog
@RequiredArgsConstructor
@Component
public class GroupBuyScheduler {

    private final GroupBuyRepository groupBuyRepository;
    private final ParticipationRepository participationRepository;

    @Scheduled(fixedRate = 60000)
    public void processExpiredGroupBuys() {
        LocalDateTime now = LocalDateTime.now();
        List<GroupBuy> expiredGroupBuys = groupBuyRepository
                .findByStatusAndEndAtBeforeAndUseYn(GroupBuyStatus.RECRUITING, now, YnCode.Y);

        for (GroupBuy groupBuy : expiredGroupBuys) {
            try {
                processGroupBuy(groupBuy);
            } catch (Exception e) {
                log.error("공동구매 처리 중 오류 발생. groupBuyNo=" + groupBuy.getGroupBuyNo(), e);
            }
        }
    }

    @Transactional
    public void processGroupBuy(GroupBuy groupBuy) {
        if (groupBuy.getCurrentQuantity() >= groupBuy.getTargetQuantity()) {
            groupBuy.setStatus(GroupBuyStatus.CONFIRMED);
            participationRepository.updateStatusByGroupBuyNo(groupBuy.getGroupBuyNo(), ParticipationStatus.CONFIRMED);
            log.info("공동구매 확정. groupBuyNo=" + groupBuy.getGroupBuyNo());
        } else {
            groupBuy.setStatus(GroupBuyStatus.AUTO_CANCELLED);
            participationRepository.updateStatusByGroupBuyNo(groupBuy.getGroupBuyNo(), ParticipationStatus.CANCELLED);
            log.info("공동구매 자동 취소. groupBuyNo=" + groupBuy.getGroupBuyNo());
        }

        groupBuy.setUpdateDate(LocalDateTime.now());
        groupBuyRepository.save(groupBuy);
    }
}
