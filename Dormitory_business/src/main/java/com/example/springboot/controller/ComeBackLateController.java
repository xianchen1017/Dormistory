package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.ComeBackLate;
import com.example.springboot.service.ComeBackLateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/come-back-late")
public class ComeBackLateController {

    @Resource
    private ComeBackLateService comeBackLateService;

    @PostMapping("/add")
    public Result add(@RequestBody ComeBackLate comeBackLate) {
        int res = comeBackLateService.add(comeBackLate);
        if (res == 1) {
            return Result.success();
        } else {
            return Result.error("400", "添加失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        int res = comeBackLateService.delete(id);
        if (res == 1) {
            return Result.success();
        } else {
            return Result.error("400", "删除失败");
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody ComeBackLate comeBackLate) {
        int res = comeBackLateService.update(comeBackLate);
        if (res == 1) {
            return Result.success();
        } else {
            return Result.error("400", "更新失败");
        }
    }

    @GetMapping("/find")
    public Result find(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) String search) {
        Page<ComeBackLate> page = comeBackLateService.find(pageNum, pageSize, search);
        return Result.success(page);
    }

    @GetMapping("/find-by-student")
    public Result findByStudent(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam String username) {
        Page<ComeBackLate> page = comeBackLateService.findByStudent(pageNum, pageSize, username);
        return Result.success(page);
    }
}