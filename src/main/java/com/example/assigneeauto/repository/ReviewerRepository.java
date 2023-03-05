package com.example.assigneeauto.repository;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    @Query("select r from Reviewer r join r.projects pi where pi.projectId = :projectId and r.isReviewAccess = :reviewAccess ")
    List<Reviewer> findAllByReviewAccessAndProjectId(boolean reviewAccess, String projectId);

    boolean existsByUsername(String username);
}