package app.com.tw.monster.controller;

import app.com.tw.monster.entity.Content;
import app.com.tw.monster.service.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(path = "/all")
    public List<Content> getAllContent() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/all/annoyance")
    public List<Content> getAllAnnoyance() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/all/diary")
    public List<Content> getAllDiary() {
        return contentService.getAllContent();
    }

    @GetMapping(path = "/{account}", produces = "application/json; charset=UTF-8")
    public List<Content> getContentByAccount(@PathVariable(name = "account") String account) {
        return contentService.getContentByAccount(account);
    }

    @PostMapping(path = "/add", produces = "application/json; charset=UTF-8")
    public String addUser(@RequestBody Content content) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            contentService.addContent(content);
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