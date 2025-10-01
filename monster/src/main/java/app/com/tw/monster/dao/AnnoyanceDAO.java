package app.com.tw.monster.dao;

import app.com.tw.monster.entity.Annoyance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnoyanceDAO extends JpaRepository<Annoyance, String> {
    Optional<Annoyance> findByApIdAndMpId(String id, String mpId);
    List<Annoyance> findByMpId(String mpId);
}