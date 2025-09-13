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

    @PostMapping(path = "/modify", produces = "application/json; charset=UTF-8")
    public String modify(@RequestBody Member member) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            member = memberService.modify(member);
            result.put("result", true);
            result.put("errorCode", 200);   
            result.put("message", "修改成功");

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

    @PostMapping(path = "/forget", produces = "application/json; charset=UTF-8")
    public String forget(@RequestBody Map<String, String> request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        String email = request.get("mpEmail");
        if (email == null || email.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "E-mail為空");
            return mapper.writeValueAsString(result);
        }
        try {
            memberService.forget(email);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "送出成功");
        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

    @PostMapping(path = "/forget_confirm", produces = "application/json; charset=UTF-8")
    public String forget_confirm(@RequestBody Map<String, String> request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        String email = request.get("mpEmail");
        String confirmCode = request.get("confirmCode");
        if (email == null || email.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "E-mail為空");
            return mapper.writeValueAsString(result);
        }else if (confirmCode == null || confirmCode.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "驗證碼為空");
        }
        try {
            memberService.confirm(email, confirmCode);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "驗證碼正確");
        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

    @PostMapping(path = "/change", produces = "application/json; charset=UTF-8")
    public String change(@RequestBody Map<String, String> request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        String email = request.get("mpEmail");
        String mpPassword = request.get("mpPassword");
        String checkPassword = request.get("checkPassword");
        if (email == null || email.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "E-mail為空");
            return mapper.writeValueAsString(result);
        }else if (mpPassword == null || mpPassword.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "密碼為空");
        }else if(checkPassword == null || checkPassword.isEmpty()) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", "確認密碼為空");
        }
        try {
            memberService.change(email, mpPassword, checkPassword);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "密碼修改成功");
        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }
}