package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.pojo.AdjustRoom;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.service.AdjustRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdjustRoomServiceImpl extends ServiceImpl<AdjustRoomMapper, AdjustRoom> implements AdjustRoomService {


    @Resource
    private AdjustRoomMapper adjustRoomMapper;

    /**
     * 添加调宿申请
     */
    @Override
    public int addApply(AdjustRoom adjustRoom) {
        System.out.println("=== 调宿申请添加服务调试信息 ===");
        System.out.println("准备插入的数据:");
        System.out.println("  学号: " + adjustRoom.getUsername());
        System.out.println("  姓名: " + adjustRoom.getName());
        System.out.println("  当前房间: " + adjustRoom.getCurrentRoomId());
        System.out.println("  当前床位: " + adjustRoom.getCurrentBedId());
        System.out.println("  目标房间: " + adjustRoom.getTowardsRoomId());
        System.out.println("  目标床位: " + adjustRoom.getTowardsBedId());
        System.out.println("  状态: " + adjustRoom.getState());
        System.out.println("  申请时间: " + adjustRoom.getApplyTime());
        
        int insert = adjustRoomMapper.insert(adjustRoom);
        System.out.println("插入结果: " + insert);
        System.out.println("=== 添加服务调试信息结束 ===");
        return insert;
    }

    /**
     * 查找调宿申请
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        System.out.println("=== 调宿申请查询调试信息 ===");
        System.out.println("查询参数 - pageNum: " + pageNum + ", pageSize: " + pageSize + ", search: '" + search + "'");
        
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        if (search != null && !search.trim().isEmpty()) {
            qw.like("username", search);
            System.out.println("添加搜索条件: username like '%" + search + "%'");
        } else {
            System.out.println("没有搜索条件，查询所有记录");
        }
        
        Page orderPage = adjustRoomMapper.selectPage(page, qw);
        System.out.println("查询结果 - 总记录数: " + orderPage.getTotal() + ", 当前页记录数: " + orderPage.getRecords().size());
        System.out.println("=== 调试信息结束 ===");
        
        return orderPage;
    }

    /**
     * 删除调宿申请
     */
    @Override
    public int deleteAdjustment(Integer id) {
        int i = adjustRoomMapper.deleteById(id);
        return i;
    }


    /**
     * 更新调宿申请
     */
    @Override
    public int updateApply(AdjustRoom adjustRoom) {
        int i = adjustRoomMapper.updateById(adjustRoom);
        return i;
    }

    /**
     * 查找所有调宿申请（调试用）
     */
    @Override
    public List<AdjustRoom> findAll() {
        return adjustRoomMapper.selectList(null);
    }


}
