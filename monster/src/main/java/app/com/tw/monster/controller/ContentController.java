package app.com.tw.monster.controller;

import app.com.tw.monster.entity.Content;
import app.com.tw.monster.security.annotation.DevOnly;
import app.com.tw.monster.service.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Map<String, Object> getContentByAccount(
            @PathVariable(name = "account") String account,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Content> contentPage = contentService.getContentByAccount(account, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        result.put("errorCode", 200);
        result.put("message", contentPage.isEmpty() ? "查無資料" : "查詢成功");
        result.put("data", contentPage.getContent());
        result.put("totalElements", contentPage.getTotalElements());
        result.put("totalPages", contentPage.getTotalPages());
        result.put("currentPage", contentPage.getNumber());
        return result;
    }

    @GetMapping(path = "/{account}/{code}", produces = "application/json; charset=UTF-8")
    public Map<String, Object> getContentByAccountAndCode(
            @PathVariable(name = "account") String account,
            @PathVariable(name = "code") String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Content> contentPage = contentService.getContentByAccountAndCode(account, code, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        result.put("errorCode", 200);
        result.put("message", contentPage.isEmpty() ? "查無資料" : "查詢成功");
        result.put("data", contentPage.getContent());
        result.put("totalElements", contentPage.getTotalElements());
        result.put("totalPages", contentPage.getTotalPages());
        result.put("currentPage", contentPage.getNumber());
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