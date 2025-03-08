package app.com.tw.monster.controller;
import app.com.tw.monster.entity.Member;
import app.com.tw.monster.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(path = "/all")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @PostMapping(path = "/register", produces = "application/json; charset=UTF-8")
    public String addUser(@RequestBody Member member) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            member = memberService.addUser(member);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "新增成功");

            ObjectNode dataNode = result.putObject("data");
            dataNode.put("account", member.getMpAccount());
            dataNode.put("email", member.getMpEmail());
            dataNode.put("birthday", member.getMpBirthday());
            dataNode.put("nickName", member.getMpNickname());

        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

    @PostMapping(path = "/login", produces = "application/json; charset=UTF-8")
    public String login(@RequestBody Map<String, String> request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        String account = request.get("mpAccount");
        String password = request.get("mpPassword");
        if (account == null || account.isEmpty() || password == null || password.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "帳號、密碼不能為空");
            return mapper.writeValueAsString(result);
        }
        try {
            Member member = memberService.login(account, password);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "登入成功");

            ObjectNode dataNode = result.putObject("data");
            dataNode.put("account", member.getMpAccount());
            dataNode.put("email", member.getMpEmail());
            dataNode.put("birthday", member.getMpBirthday());
            dataNode.put("nickName", member.getMpNickname());

        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

}