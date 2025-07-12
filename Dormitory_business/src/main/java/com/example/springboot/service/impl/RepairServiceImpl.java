package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.RepairMapper;
import com.example.springboot.service.RepairService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private RepairMapper repairMapper;

    /**
     * 添加订单
     */
    @Override
    public int addNewOrder(Repair repair) {
        int insert = repairMapper.insert(repair);
        return insert;
    }

    /**
     * 查找订单
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        Page orderPage = repairMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public Page individualFind(Integer pageNum, Integer pageSize, String search, String name) {
        System.out.println("individualFind - 查询参数: pageNum=" + pageNum + ", pageSize=" + pageSize + ", search=" + search + ", name=" + name);
        
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        // 移除用户过滤条件，显示所有报修记录
        // qw.eq("repairer", name);
        
        System.out.println("查询条件: title like " + search + " (显示所有记录)");
        
        Page orderPage = repairMapper.selectPage(page, qw);
        
        System.out.println("查询结果: 总记录数=" + orderPage.getTotal() + ", 当前页记录数=" + orderPage.getRecords().size());
        
        return orderPage;
    }

    /**
     * 更新订单
     */
    @Override
    public int updateNewOrder(Repair repair) {
        int i = repairMapper.updateById(repair);
        Assert.notNull(i, "保修单为空");
        return i;
    }

    /**
     * 删除订单
     */
    @Override
    public int deleteOrder(Integer id) {
        int i = repairMapper.deleteById(id);
        Assert.notNull(i, "保修单为空");
        return i;
    }

    /**
     * 首页顶部：报修统计
     */
    @Override
    public int showOrderNum() {
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        int orderCount = Math.toIntExact(repairMapper.selectCount(qw));
        return orderCount;
    }
}
