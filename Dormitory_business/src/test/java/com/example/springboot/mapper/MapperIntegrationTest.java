package com.example.springboot.mapper;

import com.example.springboot.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MapperIntegrationTest {

    // 注入所有Mapper
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private DormManagerMapper dormManagerMapper;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private DormBuildMapper dormBuildMapper;

    @Autowired
    private DormRoomMapper dormRoomMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private AdjustRoomMapper adjustRoomMapper;

    @Autowired
    private ComeBackLateMapper comeBackLateMapper;

    // 测试AdminMapper
    @Test
    public void testAdminMapper() {
        Admin admin = adminMapper.selectById("admin1");
        assertNotNull(admin);
        System.out.println("Admin Test: " + admin.getName());
    }

    // 测试DormManagerMapper
    @Test
    public void testDormManagerMapper() {
        DormManager manager = dormManagerMapper.selectById("dorm1");
        assertNotNull(manager);
        System.out.println("DormManager Test: " + manager.getName());
    }

    // 测试RepairMapper
    @Test
    public void testRepairMapper() {
        Repair repair = repairMapper.selectById(1);
        assertNotNull(repair);
        System.out.println("Repair Test: " + repair.getTitle());
    }

    // 测试StudentMapper
    @Test
    public void testStudentMapper() {
        Student student = studentMapper.selectById("s1001");
        assertNotNull(student);
        System.out.println("Student Test: " + student.getName());
    }

    // 测试VisitorMapper
    @Test
    public void testVisitorMapper() {
        Visitor visitor = visitorMapper.selectById(1);
        assertNotNull(visitor);
        System.out.println("Visitor Test: " + visitor.getName());
    }

    // 测试DormBuildMapper
    @Test
    public void testDormBuildMapper() {
        DormBuild build = dormBuildMapper.selectById(1);
        assertNotNull(build);
        System.out.println("DormBuild Test: " + build.getDormBuildName());
    }

    // 测试DormRoomMapper
    @Test
    public void testDormRoomMapper() {
        DormRoom room = dormRoomMapper.findByStudentUsername("s1001");
        assertNotNull(room);
        System.out.println("DormRoom Test: Room ID=" + room.getDormRoomId());
    }

    // 测试NoticeMapper
    @Test
    public void testNoticeMapper() {
        Notice notice = noticeMapper.selectById(1);
        assertNotNull(notice);
        System.out.println("Notice Test: " + notice.getTitle());
    }

    // 测试AdjustRoomMapper
    @Test
    public void testAdjustRoomMapper() {
        AdjustRoom adjustRoom = adjustRoomMapper.selectById(1);
        assertNotNull(adjustRoom);
        System.out.println("AdjustRoom Test: State=" + adjustRoom.getState());
    }

    // 测试ComeBackLateMapper
    @Test
    public void testComeBackLateMapper() {
        ComeBackLate record = comeBackLateMapper.selectById(1L);
        assertNotNull(record);
        System.out.println("ComeBackLate Test: Reason=" + record.getReason());
    }
}