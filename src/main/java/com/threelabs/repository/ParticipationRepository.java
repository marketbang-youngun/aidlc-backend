package com.threelabs.repository;

import com.threelabs.entity.Participation;
import com.threelabs.enumration.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByGroupBuyNoAndUserEmailAndStatus(Long groupBuyNo, String email, ParticipationStatus status);

    List<Participation> findByGroupBuyNoAndStatusOrderByAppendDateDesc(Long groupBuyNo, ParticipationStatus status);

    List<Participation> findByUserEmailAndStatusInOrderByAppendDateDesc(String email, List<ParticipationStatus> statuses);

    @Modifying
    @Query("UPDATE participation p SET p.status = :status WHERE p.groupBuyNo = :groupBuyNo AND p.status = 'ACTIVE'")
    int updateStatusByGroupBuyNo(@Param("groupBuyNo") Long groupBuyNo, @Param("status") ParticipationStatus status);
}
