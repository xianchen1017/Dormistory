package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.ComeBackLate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComeBackLateMapper extends BaseMapper<ComeBackLate> {
    // 这里可以添加自定义查询方法
    // 例如根据学生姓名查询晚归记录等
}