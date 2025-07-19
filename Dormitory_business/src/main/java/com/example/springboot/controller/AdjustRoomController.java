package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.pojo.AdjustRoom;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/adjustRoom")
public class AdjustRoomController {

    @Resource
    private AdjustRoomService adjustRoomService;

    @Resource
    private DormRoomService dormRoomService;


    /**
     * 添加订单
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody AdjustRoom adjustRoom) {
        System.out.println("=== 调宿申请添加接口调试信息 ===");
        System.out.println("接收到的数据:");
        System.out.println("  adjustRoom对象: " + adjustRoom);
        if (adjustRoom != null) {
            System.out.println("  ID: " + adjustRoom.getId());
            System.out.println("  学号: " + adjustRoom.getUsername());
            System.out.println("  姓名: " + adjustRoom.getName());
            System.out.println("  当前房间: " + adjustRoom.getCurrentRoomId());
            System.out.println("  当前床位: " + adjustRoom.getCurrentBedId());
            System.out.println("  目标房间: " + adjustRoom.getTowardsRoomId());
            System.out.println("  目标床位: " + adjustRoom.getTowardsBedId());
            System.out.println("  状态: " + adjustRoom.getState());
            System.out.println("  申请时间: " + adjustRoom.getApplyTime());
        }
        
        int result = adjustRoomService.addApply(adjustRoom);
        System.out.println("添加结果: " + result);
        System.out.println("=== 添加接口调试信息结束 ===");
        
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }


    /**
     * 更新订单
     */
    @PutMapping("/update/{state}")
    public Result<?> update(@RequestBody AdjustRoom adjustRoom, @PathVariable Boolean state) {
        if (state) {
            // 更新房间表信息
            int i = dormRoomService.adjustRoomUpdate(adjustRoom);
            if (i == -1) {
                return Result.error("-1", "重复操作");
            }
        }
        //更新调宿表信息
        int i = adjustRoomService.updateApply(adjustRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        int i = adjustRoomService.deleteAdjustment(id);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找订单
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        System.out.println("=== 调宿申请查询接口调试信息 ===");
        System.out.println("接收到的参数:");
        System.out.println("  pageNum: " + pageNum);
        System.out.println("  pageSize: " + pageSize);
        System.out.println("  search: '" + search + "'");
        
        Page page = adjustRoomService.find(pageNum, pageSize, search);
        
        System.out.println("查询结果:");
        System.out.println("  page对象: " + page);
        if (page != null) {
            System.out.println("  总记录数: " + page.getTotal());
            System.out.println("  当前页记录数: " + page.getRecords().size());
            System.out.println("  记录内容: " + page.getRecords());
        }
        System.out.println("=== 接口调试信息结束 ===");
        
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 查找所有调宿申请（调试用）
     */
    @GetMapping("/findAll")
    public Result<?> findAll() {
        List<AdjustRoom> list = adjustRoomService.findAll();
        return Result.success(list);
    }

    /**
     * 创建测试数据（调试用）
     */
    @PostMapping("/createTestData")
    public Result<?> createTestData(@RequestParam String username) {
        try {
            AdjustRoom testData = new AdjustRoom();
            testData.setUsername(username);
            testData.setName("测试用户");
            testData.setCurrentRoomId(101);
            testData.setCurrentBedId(1);
            testData.setTowardsRoomId(102);
            testData.setTowardsBedId(2);
            testData.setState("未处理");
            testData.setApplyTime("2025-07-19 10:00:00");
            
            int result = adjustRoomService.addApply(testData);
            if (result == 1) {
                return Result.success("测试数据创建成功");
            } else {
                return Result.error("-1", "测试数据创建失败");
            }
        } catch (Exception e) {
            return Result.error("-1", "测试数据创建异常: " + e.getMessage());
        }
    }
}
