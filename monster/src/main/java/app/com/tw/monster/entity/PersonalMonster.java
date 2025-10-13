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
@Table(name = "personal_monster")
public class PersonalMonster {
    @Id
    private String pmId;

    private String mpId = "";
    private String miId = "";
    private String pmAccessoryOne = "";
    private String pmAccessoryTwo = "";
    private String pmAccessoryThree = "";
    private String pmCreatedate;
    private String pmModifydate;

    // 用於生成唯一的 id
    public void generateCpId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("PM");
        this.pmId = generatedKey.getPk();
        this.pmCreatedate = generatedKey.getTimestamp();
        this.pmModifydate = generatedKey.getTimestamp();
    }

    public void modify() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.pmModifydate = LocalDateTime.now().format(FORMATTER);
    }
}