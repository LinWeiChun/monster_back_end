package app.com.tw.monster.service;

import app.com.tw.monster.dao.AnnoyanceDAO;
import app.com.tw.monster.dao.MemberDAO;
import app.com.tw.monster.entity.Annoyance;
import app.com.tw.monster.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnoyanceService {
    private final AnnoyanceDAO annoyanceDAO;
    private final MemberDAO memberDAO;

    @Autowired
    public AnnoyanceService(AnnoyanceDAO annoyanceDAO, MemberDAO memberDAO) {
        this.annoyanceDAO = annoyanceDAO;
        this.memberDAO = memberDAO;
    }

    public List<Annoyance> getAllAnnoyance() {
        return annoyanceDAO.findAll();
    }

    public void addAnnoyance(Annoyance annoyance) {
        if (annoyance.getMpId() == null || annoyance.getMpId().isEmpty()) {
            throw new IllegalArgumentException("請先登入後再進行煩惱紀錄！");
        } else if ((annoyance.getApContent() == null || annoyance.getApContent().isEmpty()) &&
                (annoyance.getApFile() == null || annoyance.getApFile().isEmpty())) {
            throw new IllegalArgumentException("請輸入煩惱內容！");
        } else if (annoyance.getApIndex() == null) {
            throw new IllegalArgumentException("請為您的煩惱打個分數");
        } else if (annoyance.getApShare() == null || annoyance.getApShare().isEmpty()) {
            throw new IllegalArgumentException("請選擇使否需要分享");
        } else if (memberDAO.findById(annoyance.getMpId()).isEmpty()) {
            throw new IllegalArgumentException("帳號不存在");
        }
        annoyance.generateApId();

        annoyanceDAO.save(annoyance);
    }

    public void modifyAnnoyance(String id, String account, Annoyance annoyance) {
        Member member = memberDAO.findByMpAccount(account).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("帳號資訊錯誤");
        }

        Annoyance originalAnnoyance = annoyanceDAO.findByApIdAndMpId(id, member.getMpId())
                .orElseThrow(() -> new IllegalArgumentException("煩惱資料錯誤"));

        // 更新欄位（只更新你允許修改的欄位）
        originalAnnoyance.setApSolve(annoyance.getApSolve());
        originalAnnoyance.setApShare(annoyance.getApShare());
        originalAnnoyance.modify(); // 更新修改時間

        annoyanceDAO.save(originalAnnoyance);
    }

}
