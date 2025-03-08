package app.com.tw.monster.dao;

import app.com.tw.monster.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDAO extends JpaRepository<Member, String> {
    Optional<Member> findByMpAccount(String mpAccount); // 根據帳號查詢
    Optional<Member> findByMpEmail(String mpEmail); // 根據Email查詢
}