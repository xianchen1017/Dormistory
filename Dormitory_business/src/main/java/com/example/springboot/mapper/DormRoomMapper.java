package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.DormRoom;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface DormRoomMapper extends BaseMapper<DormRoom> {
    
    /**
     * 根据学生用户名查找其所在的房间
     */
    @Select("SELECT * FROM dorm_room WHERE first_bed = #{username} OR second_bed = #{username} OR third_bed = #{username} OR fourth_bed = #{username} LIMIT 1")
    DormRoom findByStudentUsername(@Param("username") String username);
}
