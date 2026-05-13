package com.threelabs.repository;

import com.threelabs.entity.GroupBuy;
import com.threelabs.enumration.GroupBuyStatus;
import com.threelabs.enumration.code.YnCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM group_buy g WHERE g.groupBuyNo = :id")
    Optional<GroupBuy> findByIdForUpdate(@Param("id") Long id);

    List<GroupBuy> findByStatusAndEndAtBeforeAndUseYn(GroupBuyStatus status, LocalDateTime endAt, YnCode useYn);

    Page<GroupBuy> findByStatusAndUseYnOrderByAppendDateDesc(GroupBuyStatus status, YnCode useYn, Pageable pageable);

    Page<GroupBuy> findByUseYnOrderByAppendDateDesc(YnCode useYn, Pageable pageable);

    List<GroupBuy> findByStatusAndStartAtBeforeAndEndAtAfterAndUseYnOrderByAppendDateDesc(
            GroupBuyStatus status, LocalDateTime startAt, LocalDateTime endAt, YnCode useYn);
}
