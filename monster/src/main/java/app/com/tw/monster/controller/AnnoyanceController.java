package app.com.tw.monster.controller;

import app.com.tw.monster.entity.Annoyance;
import app.com.tw.monster.service.AnnoyanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/annoyance")
public class AnnoyanceController {
    private final AnnoyanceService annoyanceService;

    @Autowired
    public AnnoyanceController(AnnoyanceService annoyanceService) {
        this.annoyanceService = annoyanceService;
    }

    @GetMapping(path = "/all")
    public List<Annoyance> getAllAnnoyance() {
        return annoyanceService.getAllAnnoyance();
    }

    @GetMapping(path = "/{account}", produces = "application/json; charset=UTF-8")
    public List<Annoyance> getAnnoyanceByAccount(@PathVariable(name = "account") String account) {
        return annoyanceService.getAnnoyanceByAccount(account);
    }

    @PostMapping(path = "/add", produces = "application/json; charset=UTF-8")
    public String addUser(@RequestBody Annoyance annoyance) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            annoyanceService.addAnnoyance(annoyance);
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
            @RequestBody Annoyance annoyance) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try {
            annoyanceService.modifyAnnoyance(id, account, annoyance);

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