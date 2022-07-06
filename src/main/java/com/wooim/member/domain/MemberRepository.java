package com.wooim.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE MBR m SET m.mbrPw = :password WHERE m.mbrId = :id")
    int updatePassword(@Param("password") String password, @Param("id") Integer id);
}
