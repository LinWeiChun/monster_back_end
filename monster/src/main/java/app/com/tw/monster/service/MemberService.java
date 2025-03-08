package app.com.tw.monster.service;
import app.com.tw.monster.dao.MemberDAO;
import app.com.tw.monster.config.PasswordEncode;
import app.com.tw.monster.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberDAO memberDAO;
    PasswordEncode passwordEncoder = new PasswordEncode();

    @Autowired
    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    public Member addUser(Member member) {
        if (memberDAO.findByMpAccount(member.getMpAccount()).isPresent()) {
            throw new IllegalArgumentException("帳號已被使用！");
        }
        if (memberDAO.findByMpEmail(member.getMpEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail已被使用！");
        }
        // 2. 密碼加密
        member.setMpPassword(passwordEncoder.sha256(member.getMpPassword()));

        // 3. 產生唯一的 mp_id
        member.generateMpId();

        // 5. 存入資料庫
        return memberDAO.save(member);
    }

    public Member login(String mpAccount, String mpPassword) {
        String encryptedPassword = passwordEncoder.sha256(mpPassword);
        Member member = memberDAO.findByMpAccount(mpAccount).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("帳號不存在");
        }

        // 比對密碼是否正確
        if (!member.getMpPassword().equals(encryptedPassword)) {
            throw new IllegalArgumentException("密碼錯誤");
        }
        // 回傳會員資料
        return member;
    }
}
