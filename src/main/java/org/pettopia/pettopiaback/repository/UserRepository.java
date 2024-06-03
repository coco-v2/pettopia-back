package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.SocialType;
import org.pettopia.pettopiaback.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("select u from Users u where u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);

//    @Query("select u from Users u where u.socialId = :socialId")
//    Optional<Users> findBySocialId(@Param("socialId") String socialId);
    Optional<Users> findBySocialId(String socialId);


    @Query("SELECT u.socialType FROM Users u WHERE u.socialId = :socialId")
    SocialType findSocialTypeBySocialId(@Param("socialId") String socialId);

}
