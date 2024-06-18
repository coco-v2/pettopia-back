package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.SocialType;
import org.pettopia.pettopiaback.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(@Param("email") String email);
    Optional<Users> findBySocialId(String socialId);
    @Query("SELECT u.socialType FROM Users u WHERE u.socialId = :socialId")
    SocialType findSocialTypeBySocialId(@Param("socialId") String socialId);
    @Query("SELECT u.socialAccessToken FROM Users u WHERE u.socialId = :socialId")
    String findSocialAccessTokenBySocialId(@Param("socialId") String socialId);
}
