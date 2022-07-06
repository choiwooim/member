package com.wooim.member.controller;
import com.wooim.member.domain.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(
            @RequestParam String name) {
        return ResponseEntity.ok("hello " + name);
    }

    @RequestMapping(value = "/custom", method = RequestMethod.POST)
    public String custom() {
        return "custom";
    }

    public ResponseEntity<String> hello_post(
            @org.springframework.web.bind.annotation.RequestBody Test t
        ){
        return ResponseEntity.ok("hello " + t.getTestStr());
    }

}