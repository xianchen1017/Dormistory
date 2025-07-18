package com.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.common.Result;

import java.util.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. 本月各房型入住人数
    @GetMapping("/monthly")
    public Result<List<Map<String, Object>>> getMonthlyReport() {
        String sql = "SELECT " +
                "CASE dr.max_capacity " +
                "WHEN 1 THEN '单人间' " +
                "WHEN 2 THEN '双人间' " +
                "WHEN 4 THEN '四人间' " +
                "ELSE CONCAT(dr.max_capacity, '人间') END AS name, " +
                "COUNT(cr.id) AS value " +
                "FROM dorm_room dr " +
                "LEFT JOIN checkin_record cr " +
                "ON dr.dormroom_id = cr.room_id " +
                "AND MONTH(cr.checkin_time) = MONTH(CURDATE()) " +
                "AND YEAR(cr.checkin_time) = YEAR(CURDATE()) " +
                "GROUP BY dr.max_capacity";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("【后端查到的list】" + list);
        return Result.success(list);
    }

    // 2. 本月与去年同期入住人数对比
    @GetMapping("/monthly-compare")
    public Result<Map<String, Object>> getMonthlyCompare() {
        String sqlThisMonth = "SELECT COUNT(*) FROM checkin_record WHERE MONTH(checkin_time) = MONTH(CURDATE()) AND YEAR(checkin_time) = YEAR(CURDATE())";
        String sqlLastYear = "SELECT COUNT(*) FROM checkin_record WHERE MONTH(checkin_time) = MONTH(CURDATE()) AND YEAR(checkin_time) = YEAR(CURDATE()) - 1";
        Integer thisMonth = jdbcTemplate.queryForObject(sqlThisMonth, Integer.class);
        Integer lastYearMonth = jdbcTemplate.queryForObject(sqlLastYear, Integer.class);
        Map<String, Object> map = new HashMap<>();
        map.put("thisMonth", thisMonth);
        map.put("lastYearMonth", lastYearMonth);
        return Result.success(map);
    }

    // 3. 本季度每月入住人数
    @GetMapping("/quarterly")
    public Result<List<Map<String, Object>>> getQuarterly() {
        String sql = "SELECT MONTH(checkin_time) AS month, COUNT(*) AS total " +
                "FROM checkin_record " +
                "WHERE QUARTER(checkin_time) = QUARTER(CURDATE()) AND YEAR(checkin_time) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(checkin_time) " +
                "ORDER BY month";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return Result.success(list);
    }

    // 4. 全年每月入住人数
    @GetMapping("/yearly")
    public Result<List<Map<String, Object>>> getYearly() {
        String sql = "SELECT MONTH(checkin_time) AS month, COUNT(*) AS total " +
                "FROM checkin_record " +
                "WHERE YEAR(checkin_time) = 2025 " +
                "GROUP BY MONTH(checkin_time) " +
                "ORDER BY month";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return Result.success(list);
    }
}