package com.example.markservice.controller;

import com.example.markservice.exception.UserDoesNotExistException;
import com.example.markservice.service.MarkService;
import com.example.markservice.service.MarkServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/marks")
@Api(description = "Controller for work with marks")
public class MarkController {
    private final MarkService service;

    @Autowired
    public MarkController(MarkServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation("Getting all marks")
    public ResponseEntity<?> getMarks(@RequestBody @ApiParam(
            name = "Login",
            type = "Map",
            value = "Login of the user",
            example = "{\n\t\"login\" : \"testlogin@mail.com\"\n}",
            required = true) Map<String, String> credentials){
        try{
            return ResponseEntity.ok(service.getMarks(credentials.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
