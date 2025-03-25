package com.goorm.clonestagram.file.repository;

import com.goorm.clonestagram.file.domain.SoftDelete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SoftDeleteRepository extends JpaRepository<SoftDelete, Long> {
    List<SoftDelete> findByDeletedAtBefore(LocalDateTime threshold);
}
