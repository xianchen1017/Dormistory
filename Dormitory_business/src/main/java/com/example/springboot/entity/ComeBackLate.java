package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName(value = "come_back_late")
public class ComeBackLate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("student_name")
    private String studentName;

    @TableField("dorm_room")
    private String dormRoom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("late_time")
    private Date lateTime;

    @TableField("reason")
    private String reason;

    @TableField("remark")
    private String remark;
}