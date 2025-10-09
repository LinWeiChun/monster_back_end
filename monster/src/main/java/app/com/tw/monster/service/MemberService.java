package app.com.tw.monster.service;
import app.com.tw.monster.config.Transform;
import app.com.tw.monster.dao.MemberDAO;
import app.com.tw.monster.config.SetCode;
import app.com.tw.monster.entity.Member;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberDAO memberDAO;
    SetCode setCode = new SetCode();
    Transform transform = new Transform();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    public Member addUser(Member member) {
        if(member.getMpAccount()==null || member.getMpAccount().isEmpty()){
            throw new IllegalArgumentException("帳號不能為空！");
        }else if (memberDAO.findByMpAccount(member.getMpAccount()).isPresent()) {
            throw new IllegalArgumentException("帳號已被使用！");
        }else if(member.getMpPassword()==null || member.getMpPassword().isEmpty()) {
            throw new IllegalArgumentException("密碼不能為空！");
        }else if(member.getMpName()==null || member.getMpName().isEmpty()) {
            throw new IllegalArgumentException("名稱不能為空！");
        }else if(member.getMpEmail() == null || member.getMpEmail().isEmpty()) {
            throw new IllegalArgumentException("E-mail不能為空！");
        }else if (memberDAO.findByMpEmail(member.getMpEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail已被使用！");
        }else if(member.getMpBirthday()==null || member.getMpBirthday().isEmpty()) {
            throw new IllegalArgumentException("生日不能為空！");
        }
        // 2. 密碼加密
        member.setMpPassword(setCode.sha256(member.getMpPassword()));

        member.setMpBirthday(transform.formatDate(member.getMpBirthday()));

        // 3. 產生唯一的 mp_id
        member.generateMpId();

        // 5. 存入資料庫
        return memberDAO.save(member);
    }

    public Member login(String mpAccount, String mpPassword) {
        String encryptedPassword = setCode.sha256(mpPassword);
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

    public Member modify(Member member) {
        // 驗證輸入的會員資訊是否完整
        if (member == null || member.getMpAccount() == null) {
            throw new IllegalArgumentException("會員資訊或會員帳號不得為空！");
        }

        // 查詢資料庫中是否存在該會員
        Optional<Member> existingMemberOpt = memberDAO.findByMpAccount(member.getMpAccount());
        if (existingMemberOpt.isEmpty()) {
            throw new IllegalArgumentException("找不到對應的會員資訊！");
        }

        // 取得原有會員資料
        Member existingMember = existingMemberOpt.get();

//        System.out.println(member.getMpPassword() == null && member.getMpPassword().equals(""));
        // 僅更新 JSON 中不為 null 的欄位
        if (!(member.getMpPassword() == null && member.getMpPassword().equals(""))) {
            existingMember.setMpPassword(setCode.sha256(member.getMpPassword()));
        }
        if (!(member.getMpBirthday() != null && member.getMpBirthday().equals(""))) {
            existingMember.setMpBirthday(transform.formatDate(member.getMpBirthday()));
        }
        if (!(member.getMpNickname() != null && member.getMpNickname().equals(""))) {
            existingMember.setMpNickname(member.getMpNickname());
        }

        // 更新時間戳
        existingMember.modify();

        // 保存更新後的會員資料
        return memberDAO.save(existingMember);
    }

    public void forget(String mpEmail) {
        Member member = memberDAO.findByMpEmail(mpEmail).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("E-mail不存在");
        }
        if (member.getMpId() != null && memberDAO.existsById(member.getMpId())) {
            String confirmCode = setCode.confirmCode();
            member.setMpConfirmCode(confirmCode);
            memberDAO.save(member);

            // 寄送信件
            try {
                sendResetEmail(mpEmail, confirmCode);
            } catch (MessagingException e) {
                throw new RuntimeException("寄信失敗", e);
            }
        } else {
            throw new IllegalArgumentException("會員資訊錯誤！");
        }
    }
    public void confirm(String mpEmail, String confirmCode) {
        Member member = memberDAO.findByMpEmail(mpEmail).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("E-mail不存在");
        }
        if (!confirmCode.equals(member.getMpConfirmCode())){
            throw new IllegalArgumentException("驗證碼錯誤");
        }
    }

    public void change(String mpEmail, String mpPassword, String checkPassword) {
        Member member = memberDAO.findByMpEmail(mpEmail).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("E-mail不存在");
        }
        if (!mpPassword.equals(checkPassword)){
            throw new IllegalArgumentException("密碼與驗證驗碼不同");
        }
        String encryptedPassword = setCode.sha256(mpPassword);
        if(member.getMpPassword().equals(encryptedPassword)){
            throw new IllegalArgumentException("新密碼需與前次不同");
        }
        member.setMpPassword(encryptedPassword);
        memberDAO.save(member);
    }


    private void sendResetEmail(String to, String confirmCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("linweijun0528@gmail.com");
        helper.setTo(to);
        helper.setSubject("重設密碼確認碼");
        helper.setText(
                "<h3>親愛的會員您好：</h3>" +
                        "<p>您的確認碼如下：</p>" +
                        "<h2>" + confirmCode + "</h2>" +
                        "<p>請使用此確認碼來重設您的密碼。</p>",
                true
        );

        mailSender.send(message);
    }
}
