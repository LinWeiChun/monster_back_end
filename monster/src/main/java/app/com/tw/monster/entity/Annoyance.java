package app.com.tw.monster.entity;

import app.com.tw.monster.config.PrimaryKeyGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "annoyance_profile")
public class Annoyance {
    @Id
    private String apId;

    private String mpId = "";
    private String apContent = "";
    private String apFile = "";
    private Integer apType = 0;
    private String moId = "";
    private String apMood = "";
    private Integer apIndex = 1;
    private String apSolve = "N";
    private String apShare = "";
    private String apCreatedate;
    private String apModifydate;

    // 用於生成唯一的 mp_id
    public void generateApId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("AP");
        this.apId = generatedKey.getPk();
        this.apCreatedate = generatedKey.getTimestamp();
        this.apModifydate = generatedKey.getTimestamp();
    }

    public void modify() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.apModifydate = LocalDateTime.now().format(FORMATTER);
    }
}