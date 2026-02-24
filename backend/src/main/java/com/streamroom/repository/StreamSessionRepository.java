package com.streamroom.repository;

import com.streamroom.entity.StreamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamSessionRepository extends JpaRepository<StreamSession, Long> {
    List<StreamSession> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<StreamSession> findByIsLiveTrue();
    List<StreamSession> findByIsLiveFalseOrderByEndedAtDesc(int limit);
}
