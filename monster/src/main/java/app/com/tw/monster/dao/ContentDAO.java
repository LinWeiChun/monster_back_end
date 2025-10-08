package app.com.tw.monster.dao;

import app.com.tw.monster.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentDAO extends JpaRepository<Content, String> {
    Optional<Content> findByCpIdAndMpId(String id, String mpId);
    List<Content> findByMpId(String mpId);
    List<Content> findByCpCode(String cpCode);
}