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
@Table(name = "member_profile")
public class Member {
    @Id
    private String mpId;

    private String mpAccount = "";
    private String mpPassword = "";
    private String mpName = "";
    private String mpEmail = "";
    private String mpBirthday = "";
    private String mpNickname = "";
    private String pmId = "";
    private String mpConfirmCode = "";
    private String mpStatus = "Y";
    private String mpCreatedate;
    private String mpModifydate;

    // 用於生成唯一的 mp_id
    public void generateMpId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("MP");
        this.mpId = generatedKey.getPk();
        this.mpCreatedate = generatedKey.getTimestamp();
        this.mpModifydate = generatedKey.getTimestamp();
    }

    public void modify() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.mpModifydate = LocalDateTime.now().format(FORMATTER);
    }
}