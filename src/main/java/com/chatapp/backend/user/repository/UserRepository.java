package com.chatapp.backend.user.repository;

import com.chatapp.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query(value = "SELECT * FROM users u WHERE u.id != :currentUserId AND (:q IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))", nativeQuery = true)
    Page<User> searchAllExceptCurrent(@Param("currentUserId") UUID currentUserId,
                                      @Param("q") String query,
                                      Pageable pageable);
}
