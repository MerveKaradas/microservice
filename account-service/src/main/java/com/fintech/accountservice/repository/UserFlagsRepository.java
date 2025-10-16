package com.fintech.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fintech.accountservice.model.entity.UserFlag;

import java.util.UUID;

import javax.transaction.Transactional;

@Repository
public interface UserFlagsRepository extends JpaRepository<UserFlag, UUID> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_flags SET profile_complete = true WHERE user_id = :userId", nativeQuery = true)
    void markProfileComplete(UUID userId);

    boolean existsByUserIdAndProfileCompleteTrue(UUID userId);
}