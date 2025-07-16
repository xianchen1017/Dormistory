package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.ComeBackLate;
import com.example.springboot.mapper.ComeBackLateMapper;
import com.example.springboot.service.ComeBackLateService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ComeBackLateServiceImpl extends ServiceImpl<ComeBackLateMapper, ComeBackLate> implements ComeBackLateService {

    @Override
    public Page<ComeBackLate> find(Integer pageNum, Integer pageSize, String search) {
        QueryWrapper<ComeBackLate> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("student_name", search)
                    .or().like("dorm_build_name", search)
                    .or().like("reason", search);
        }
        queryWrapper.orderByDesc("late_time"); // 改为按 late_time 排序
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public int add(ComeBackLate comeBackLate) {
        return this.save(comeBackLate) ? 1 : 0;
    }

    @Override
    public int update(ComeBackLate comeBackLate) {
        return this.updateById(comeBackLate) ? 1 : 0;
    }

    @Override
    public int delete(Integer id) {
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public Page<ComeBackLate> findByStudent(Integer pageNum, Integer pageSize, String username) {
        QueryWrapper<ComeBackLate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_username", username)
                .orderByDesc("create_time");
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }
}