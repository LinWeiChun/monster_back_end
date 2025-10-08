package app.com.tw.monster.controller;

import app.com.tw.monster.entity.Content;
import app.com.tw.monster.security.annotation.DevOnly;
import app.com.tw.monster.service.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(path = "/all")
    @DevOnly
    public List<Content> getAllContent() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/all/annoyance")
    @DevOnly
    public List<Content> getAllAnnoyance() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/all/diary")
    @DevOnly
    public List<Content> getAllDiary() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/{account}", produces = "application/json; charset=UTF-8")
    public Map<String, Object> getContentByAccount(@PathVariable(name = "account") String account) {
        List<Content> contentList = contentService.getContentByAccount(account);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        result.put("errorCode", 200);
        result.put("message", contentList.isEmpty() ? "查無資料" : "查尋成功");
        result.put("data", contentList);
        return result;
    }
    @GetMapping(path = "/{account}/{code}", produces = "application/json; charset=UTF-8")
    public Map<String, Object>  getContentByAccountAndCode(@PathVariable(name = "account") String account,@PathVariable(name = "code") String code) {
        List<Content> contentList = contentService.getContentByAccountAndCode(account, code);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        result.put("errorCode", 200);
        result.put("message", contentList.isEmpty() ? "查無資料" : "查尋成功");
        result.put("data", contentList);
        return result;
    }


    @PostMapping(path = "/add/{account}", produces = "application/json; charset=UTF-8")
    public String addUser(
            @PathVariable(name = "account") String account,
            @RequestBody Content content) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            contentService.addContent(account, content);
            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "新增成功");

        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

    @PostMapping(path = "/modify/{id}/{account}", produces = "application/json; charset=UTF-8")
    public String modifyAnnoyance(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "account") String account,
            @RequestBody Content content) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            contentService.modifyAnnoyance(id, account, content);

            result.put("result", true);
            result.put("errorCode", 200);
            result.put("message", "修改成功");

        } catch (IllegalArgumentException e) {
            result.put("result", false);
            result.put("errorCode", 400);
            result.put("message", e.getMessage());
        }

        return mapper.writeValueAsString(result);
    }

}