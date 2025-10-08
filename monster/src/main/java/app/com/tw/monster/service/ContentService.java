package app.com.tw.monster.service;

import app.com.tw.monster.dao.ContentDAO;
import app.com.tw.monster.dao.MemberDAO;
import app.com.tw.monster.entity.Content;
import app.com.tw.monster.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {
    private final ContentDAO contentDAO;
    private final MemberDAO memberDAO;

    @Autowired
    public ContentService(ContentDAO contentDAO, MemberDAO memberDAO) {
        this.contentDAO = contentDAO;
        this.memberDAO = memberDAO;
    }

    public List<Content> getAllContent() {
        return contentDAO.findAll();
    }

    public List<Content> getAllAnnoyance() {
        return contentDAO.findByCpCode("annoyance");
    }

    public List<Content> getAllDiary() {
        return contentDAO.findByCpCode("diary");
    }

    public List<Content> getContentByAccount(String account) {
        Member member = memberDAO.findByMpAccount(account).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("帳號資訊錯誤");
        }
        return contentDAO.findByMpId(member.getMpId());
    }

    public void addContent(Content content) {
        if (content.getMpId() == null || content.getMpId().isEmpty()) {
            throw new IllegalArgumentException("請先登入後再進行煩惱紀錄或日記填寫！");
        } else if ((content.getCpContent() == null || content.getCpContent().isEmpty()) &&
                (content.getCpFile() == null || content.getCpFile().isEmpty())) {
            throw new IllegalArgumentException("請輸入煩惱/日記內容！");
        } else if (content.getCpIndex() == null && content.getCpCode().equals("annoyance")) {
            throw new IllegalArgumentException("請為您的煩惱打個分數");
        } else if (content.getCpShare() == null || content.getCpShare().isEmpty()) {
            throw new IllegalArgumentException("請選擇使否需要分享");
        } else if (memberDAO.findById(content.getMpId()).isEmpty()) {
            throw new IllegalArgumentException("帳號不存在");
        }
        content.generateCpId();

        contentDAO.save(content);
    }

    public void modifyAnnoyance(String id, String account, Content content) {
        Member member = memberDAO.findByMpAccount(account).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("帳號資訊錯誤");
        }

        Content originalContent = contentDAO.findByCpIdAndMpId(id, member.getMpId())
                .orElseThrow(() -> new IllegalArgumentException("煩惱資料錯誤"));

        // 更新欄位（只更新你允許修改的欄位）
        originalContent.setCpSolve(content.getCpSolve());
        originalContent.setCpShare(content.getCpShare());
        originalContent.modify(); // 更新修改時間

        contentDAO.save(originalContent);
    }

}
