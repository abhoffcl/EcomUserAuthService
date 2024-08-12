package dev.Abhishek.EcomUserAuthService.repository;



import dev.Abhishek.EcomUserAuthService.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByValueAndDeletedEquals(String tokenValue, Boolean isDeleted);
    Optional<Token> findByValueAndDeletedEqualsAndExpireAtGreaterThan(String tokenValue, Boolean isDeleted, Instant instant);
}
