package app.com.tw.monster.entity;

import app.com.tw.monster.config.PrimaryKeyGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "member_profile")
public class Member {
    @Id
    private String mpId;

    private String mpAccount;
    private String mpPassword;
    private String mpEmail;
    private String mpBirthday;
    private String mpNickname;
    private String mpCreatedate;
    private String mpModifydate;

    // 用於生成唯一的 mp_id
    public void generateMpId() {
        PrimaryKeyGenerator.GeneratedKey generatedKey = PrimaryKeyGenerator.generateKey("MP");
        this.mpId = generatedKey.getPk();
        this.mpCreatedate = generatedKey.getTimestamp();
        this.mpModifydate = generatedKey.getTimestamp();
    }
}