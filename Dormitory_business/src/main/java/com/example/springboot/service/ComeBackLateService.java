package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.ComeBackLate;

public interface ComeBackLateService extends IService<ComeBackLate> {

    // 分页查询晚归记录
    Page<ComeBackLate> find(Integer pageNum, Integer pageSize, String search);

    // 新增晚归记录
    int add(ComeBackLate comeBackLate);

    // 更新晚归记录
    int update(ComeBackLate comeBackLate);

    // 删除晚归记录
    int delete(Integer id);

    // 根据学生用户名查询晚归记录
    Page<ComeBackLate> findByStudent(Integer pageNum, Integer pageSize, String username);
}