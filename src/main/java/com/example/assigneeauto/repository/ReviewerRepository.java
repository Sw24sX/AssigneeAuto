package com.example.assigneeauto.repository;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    Optional<Reviewer> findByUsernameOrGitUsername(String username, String gitUsername);

    Optional<Reviewer> findByUsername(String username);
    @Query("select r from Reviewer r where r.isReviewAccess = :reviewAccess")
    List<Reviewer> findAllByReviewAccess(boolean reviewAccess);
}