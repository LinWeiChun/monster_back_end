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
@Table(name = "content_profile")
public class Content {
    @Id
    private String cpId;

    private String mpId = "";
    private String cpContent = "";
    private String cpFile = "";
    private Integer cpType = 0;
    private String miId = "";
    private String cpMood = "";
    private Integer cpIndex = 1;
    private String cpSolve = "N";
    private String cpShare = "";
    private String cpCode = "";
    private String cpCreatedate;
    private String cpModifydate;

    // 用於生成唯一的 id
    public void generateCpId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("CP");
        this.cpId = generatedKey.getPk();
        this.cpCreatedate = generatedKey.getTimestamp();
        this.cpModifydate = generatedKey.getTimestamp();
    }

    public void modify() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.cpModifydate = LocalDateTime.now().format(FORMATTER);
    }
}