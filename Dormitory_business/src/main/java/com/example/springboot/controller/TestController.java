package com.example.springboot.controller;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/hello")
    public Result<?> hello(@RequestBody Object data) {
        return Result.success("Hello from backend! Data: " + data);
    }
    
    @GetMapping("/hello")
    public Result<?> helloGet() {
        return Result.success("Hello from backend GET!");
    }
} 