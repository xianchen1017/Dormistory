package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.AdjustRoom;

import java.util.List;

public interface AdjustRoomService extends IService<AdjustRoom> {

    //查询调宿申请
    Page find(Integer pageNum, Integer pageSize, String search);

    //删除调宿申请
    int deleteAdjustment(Integer id);

    //更新
    int updateApply(AdjustRoom adjustRoom);

    // 添加
    int addApply(AdjustRoom adjustRoom);
    
    // 查找所有调宿申请（调试用）
    List<AdjustRoom> findAll();

}
