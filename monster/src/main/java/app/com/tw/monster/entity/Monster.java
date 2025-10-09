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
@Table(name = "monster_information")
public class Monster {
    @Id
    private String miId;

    private String miName = "";
    private String miNameEn = "";
    private String miPhoto = "";
    private String miAvatar = "";
    private String miGifRight = "";
    private String miGifLeft = "";
    private String miAccessoryOne = "";
    private String miAccessoryTwo = "";
    private String miAccessoryThree = "";
    private String miCreatedate;
    private String miModifydate;

    // 用於生成唯一的 id
    public void generateCpId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("MI");
        this.miId = generatedKey.getPk();
        this.miCreatedate = generatedKey.getTimestamp();
        this.miModifydate = generatedKey.getTimestamp();
    }

    public void modify() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.miModifydate = LocalDateTime.now().format(FORMATTER);
    }
}