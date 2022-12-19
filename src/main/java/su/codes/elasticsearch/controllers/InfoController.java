package su.codes.elasticsearch.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.codes.elasticsearch.models.Info;
import su.codes.elasticsearch.services.InfoService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @PostMapping()
    public ResponseEntity<String> addInfo(@RequestBody Info info) throws IOException {
        return new ResponseEntity<>(infoService.addInfo(info), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Info> getInfo(@PathVariable("id") String id) throws IOException {
        return new ResponseEntity<>(infoService.getInfo(id), HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> addMultipleInfoRecords(@RequestBody List<Info> infoList) throws IOException {
        return new ResponseEntity<>(infoService.addMultipleInfoRecords(infoList), HttpStatus.CREATED);
    }
}
