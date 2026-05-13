package com.threelabs.repository;

import com.threelabs.entity.AdminUser;
import com.threelabs.enumration.code.YnCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUserIdAndUseYn(String userId, YnCode useYn);
}
