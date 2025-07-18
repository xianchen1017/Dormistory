package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.JudgeBedName;
import com.example.springboot.pojo.AdjustRoom;
import com.example.springboot.pojo.DormRoom;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.service.DormRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.example.springboot.common.CalPeopleNum.calNum;


@Service
public class DormRoomImpl extends ServiceImpl<DormRoomMapper, DormRoom> implements DormRoomService {

    @Resource
    private DormRoomMapper dormRoomMapper;

    /**
     * 首页顶部：空宿舍统计
     */
    @Override
    public int notFullRoom() {
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.lt("current_capacity", 4);
        int notFullRoomNum = Math.toIntExact(dormRoomMapper.selectCount(qw));
        return notFullRoomNum;
    }

    /**
     * 添加房间
     */
    @Override
    public int addNewRoom(DormRoom dormRoom) {
        int insert = dormRoomMapper.insert(dormRoom);
        return insert;
    }

    /**
     * 查找房间
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.like("Dormroom_id", search);
        Page roomPage = dormRoomMapper.selectPage(page, qw);
        return roomPage;
    }

    /**
     * 更新房间
     */
    @Override
    public int updateNewRoom(DormRoom dormRoom) {
        int i = dormRoomMapper.updateById(dormRoom);
        return i;
    }

    /**
     * 删除房间
     */
    @Override
    public int deleteRoom(Integer dormRoomId) {
        int i = dormRoomMapper.deleteById(dormRoomId);
        return i;
    }

    /**
     * 删除床位上的学生信息
     */
    @Override
    public int deleteBedInfo(String bedName, Integer dormRoomId, int calCurrentNum) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("dormroom_id", dormRoomId);
        updateWrapper.set(bedName, null);
        updateWrapper.set("current_capacity", calCurrentNum - 1);
        int update = dormRoomMapper.update(null, updateWrapper);
        return update;

    }

    /**
     * 床位信息，查询该学生是否已由床位
     */
    @Override
    public DormRoom judgeHadBed(String username) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("first_bed", username);
        qw.or();
        qw.eq("second_bed", username);
        qw.or();
        qw.eq("third_bed", username);
        qw.or();
        qw.eq("fourth_bed", username);
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 主页 住宿人数
     */
    @Override
    public Long selectHaveRoomStuNum() {
        QueryWrapper<DormRoom> qw1 = new QueryWrapper<>();
        qw1.isNotNull("first_bed");
        Long first_bed = dormRoomMapper.selectCount(qw1);

        QueryWrapper<DormRoom> qw2 = new QueryWrapper<>();
        qw2.isNotNull("second_bed");
        Long second_bed = dormRoomMapper.selectCount(qw2);

        QueryWrapper<DormRoom> qw3 = new QueryWrapper<>();
        qw3.isNotNull("third_bed");
        Long third_bed = dormRoomMapper.selectCount(qw3);

        QueryWrapper<DormRoom> qw4 = new QueryWrapper<>();
        qw4.isNotNull("fourth_bed");
        Long fourth_bed = dormRoomMapper.selectCount(qw4);

        Long count = first_bed + second_bed + third_bed + fourth_bed;
        return count;
    }

    /**
     * 获取每栋宿舍学生总人数
     */
    @Override
    public Long getEachBuildingStuNum(int dormBuildId) {

        QueryWrapper<DormRoom> qw1 = new QueryWrapper<>();
        qw1.eq("dormbuild_id", dormBuildId);
        qw1.isNotNull("first_bed");
        Long first_bed = dormRoomMapper.selectCount(qw1);

        QueryWrapper<DormRoom> qw2 = new QueryWrapper<>();
        qw2.eq("dormbuild_id", dormBuildId);
        qw2.isNotNull("second_bed");
        Long second_bed = dormRoomMapper.selectCount(qw2);

        QueryWrapper<DormRoom> qw3 = new QueryWrapper<>();
        qw3.eq("dormbuild_id", dormBuildId);
        qw3.isNotNull("third_bed");
        Long third_bed = dormRoomMapper.selectCount(qw3);

        QueryWrapper<DormRoom> qw4 = new QueryWrapper<>();
        qw4.eq("dormbuild_id", dormBuildId);
        qw4.isNotNull("fourth_bed");
        Long fourth_bed = dormRoomMapper.selectCount(qw4);

        Long count = first_bed + second_bed + third_bed + fourth_bed;

        return count;
    }

    /**
     * 根据调宿申请表对房间表内的学生床位进行调整
     */
    @Override
    public int adjustRoomUpdate(AdjustRoom adjustRoom) {
        //调宿人
        String username = adjustRoom.getUsername();
        //当前房间号
        int currentRoomId = adjustRoom.getCurrentRoomId();
        //当前床位名称
        String currentBedName = JudgeBedName.getBedName(adjustRoom.getCurrentBedId());
        //目标房间号
        int towardsRoomId = adjustRoom.getTowardsRoomId();
        //目标目标房间号
        String towardsBedName = JudgeBedName.getBedName(adjustRoom.getTowardsBedId());
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", currentRoomId);
        qw.isNotNull(currentBedName);
        DormRoom dormRoom1 = dormRoomMapper.selectOne(qw);
        if (dormRoom1 == null) {
            return -2;
        }
        int currentCapacity1 = calNum(dormRoom1);
        UpdateWrapper uw1 = new UpdateWrapper();
        uw1.eq("dormroom_id", currentRoomId);
        uw1.set(currentBedName, null);
        uw1.set("current_capacity", currentCapacity1 - 1);
        int result1 = dormRoomMapper.update(null, uw1);
        DormRoom dormRoom2 = dormRoomMapper.selectById(towardsRoomId);
        int currentCapacity2 = calNum(dormRoom2);
        if (result1 == 1) {
            UpdateWrapper uw2 = new UpdateWrapper();
            uw2.eq("dormroom_id", towardsRoomId);
            uw2.set(towardsBedName, username);
            uw2.set("current_capacity", currentCapacity2 + 1);
            int result2 = dormRoomMapper.update(null, uw2);
            return result2;
        }
        return -1;
    }


    /**
     * 检查该房间是否满了
     */
    @Override
    public DormRoom checkRoomState(Integer dormRoomId) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", dormRoomId);
        qw.lt("current_capacity", 4);
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 检查该房间是否存在
     */
    @Override
    public DormRoom checkRoomExist(Integer dormRoomId) {
        DormRoom dormRoom = dormRoomMapper.selectById(dormRoomId);
        return dormRoom;
    }


    /**
     * 检查床位是否有人
     */
    @Override
    public DormRoom checkBedState(Integer dormRoomId, int bedNum) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", dormRoomId);
        qw.isNull(JudgeBedName.getBedName(bedNum));
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 根据学生用户名释放床位
     */
    @Override
    public void releaseBedByStudent(String username) {
        System.out.println("开始释放学生 " + username + " 的床位...");
        
        // 1. 查找该学生所在的房间
        DormRoom dormRoom = dormRoomMapper.findByStudentUsername(username);
        if (dormRoom == null) {
            System.out.println("学生 " + username + " 没有分配房间，无需释放床位");
            return; // 学生没有分配房间，直接返回
        }
        
        System.out.println("找到学生 " + username + " 所在的房间，房间ID: " + dormRoom.getDormRoomId());
        System.out.println("当前床位信息：first_bed=" + dormRoom.getFirstBed() + 
                          ", second_bed=" + dormRoom.getSecondBed() + 
                          ", third_bed=" + dormRoom.getThirdBed() + 
                          ", fourth_bed=" + dormRoom.getFourthBed());
        
        // 2. 使用UpdateWrapper来确保字段被正确设置为null
        UpdateWrapper<DormRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("dormroom_id", dormRoom.getDormRoomId());
        
        // 3. 找到对应床位并置空
        String bedField = null;
        if (username.equals(dormRoom.getFirstBed())) {
            updateWrapper.set("first_bed", null);
            bedField = "first_bed";
        } else if (username.equals(dormRoom.getSecondBed())) {
            updateWrapper.set("second_bed", null);
            bedField = "second_bed";
        } else if (username.equals(dormRoom.getThirdBed())) {
            updateWrapper.set("third_bed", null);
            bedField = "third_bed";
        } else if (username.equals(dormRoom.getFourthBed())) {
            updateWrapper.set("fourth_bed", null);
            bedField = "fourth_bed";
        }
        
        // 4. 更新房间当前人数
        int newCapacity = dormRoom.getCurrentCapacity() - 1;
        updateWrapper.set("current_capacity", newCapacity);
        
        // 5. 执行更新操作
        int result = dormRoomMapper.update(null, updateWrapper);
        
        if (result > 0) {
            System.out.println("成功释放学生 " + username + " 的床位，房间ID: " + dormRoom.getDormRoomId() + 
                              "，床位字段: " + bedField + "，新人数: " + newCapacity);
        } else {
            System.out.println("释放学生 " + username + " 床位失败，更新结果: " + result);
        }
    }
    @Override
    public int addRoomEvaluation(Integer dormRoomId, String evaluation) {
        UpdateWrapper<DormRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("dormroom_id", dormRoomId);
        updateWrapper.set("evaluation", evaluation);
        return dormRoomMapper.update(null, updateWrapper);
    }

    @Override
    public DormRoom getRoomEvaluation(Integer dormRoomId) {
        QueryWrapper<DormRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dormroom_id", dormRoomId);
        return dormRoomMapper.selectOne(queryWrapper);
    }
}
