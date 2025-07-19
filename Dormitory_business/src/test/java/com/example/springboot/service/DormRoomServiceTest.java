package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.DormRoom;
import com.example.springboot.service.impl.DormRoomImpl;
import com.example.springboot.mapper.DormRoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DormRoomServiceTest {

    @Mock
    private DormRoomMapper dormRoomMapper;

    @InjectMocks
    private DormRoomImpl dormRoomService;

    private DormRoom testDormRoom;

    @BeforeEach
    void setUp() {
        testDormRoom = new DormRoom();
        testDormRoom.setDormRoomId(101);
        testDormRoom.setDormBuildId(1);
        testDormRoom.setFloorNum(1);
        testDormRoom.setMaxCapacity(4);
        testDormRoom.setCurrentCapacity(2);
        testDormRoom.setFirstBed("张三");
        testDormRoom.setSecondBed("李四");
        testDormRoom.setThirdBed("");
        testDormRoom.setFourthBed("");
        testDormRoom.setEvaluation("很好");
    }

    @Test
    void testNotFullRoom_Success() {
        // Given
        when(dormRoomMapper.selectCount(any())).thenReturn(10L);

        // When
        int result = dormRoomService.notFullRoom();

        // Then
        assertEquals(10, result);
        verify(dormRoomMapper).selectCount(any());
    }

    @Test
    void testAddNewRoom_Success() {
        // Given
        when(dormRoomMapper.insert(any(DormRoom.class))).thenReturn(1);

        // When
        int result = dormRoomService.addNewRoom(testDormRoom);

        // Then
        assertEquals(1, result);
        verify(dormRoomMapper).insert(testDormRoom);
    }

    @Test
    void testAddNewRoom_Failure() {
        // Given
        when(dormRoomMapper.insert(any(DormRoom.class))).thenReturn(0);

        // When
        int result = dormRoomService.addNewRoom(testDormRoom);

        // Then
        assertEquals(0, result);
        verify(dormRoomMapper).insert(testDormRoom);
    }

    @Test
    void testFind_Success() {
        // Given
        Page<DormRoom> page = new Page<>(1, 10);
        List<DormRoom> rooms = Arrays.asList(testDormRoom);
        page.setRecords(rooms);
        page.setTotal(1);
        
        when(dormRoomMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<DormRoom> result = dormRoomService.find(1, 10, "");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(dormRoomMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithSearch() {
        // Given
        Page<DormRoom> page = new Page<>(1, 10);
        List<DormRoom> rooms = Arrays.asList(testDormRoom);
        page.setRecords(rooms);
        page.setTotal(1);
        
        when(dormRoomMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<DormRoom> result = dormRoomService.find(1, 10, "101");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(dormRoomMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateNewRoom_Success() {
        // Given
        when(dormRoomMapper.updateById(any(DormRoom.class))).thenReturn(1);

        // When
        int result = dormRoomService.updateNewRoom(testDormRoom);

        // Then
        assertEquals(1, result);
        verify(dormRoomMapper).updateById(testDormRoom);
    }

    @Test
    void testUpdateNewRoom_Failure() {
        // Given
        when(dormRoomMapper.updateById(any(DormRoom.class))).thenReturn(0);

        // When
        int result = dormRoomService.updateNewRoom(testDormRoom);

        // Then
        assertEquals(0, result);
        verify(dormRoomMapper).updateById(testDormRoom);
    }

    @Test
    void testDeleteRoom_Success() {
        // Given
        when(dormRoomMapper.deleteById(101)).thenReturn(1);

        // When
        int result = dormRoomService.deleteRoom(101);

        // Then
        assertEquals(1, result);
        verify(dormRoomMapper).deleteById(101);
    }

    @Test
    void testDeleteRoom_Failure() {
        // Given
        when(dormRoomMapper.deleteById(101)).thenReturn(0);

        // When
        int result = dormRoomService.deleteRoom(101);

        // Then
        assertEquals(0, result);
        verify(dormRoomMapper).deleteById(101);
    }

    @Test
    void testDeleteBedInfo_Success() {
        // Given
        when(dormRoomMapper.update(any(), any())).thenReturn(1);

        // When
        int result = dormRoomService.deleteBedInfo("床1", 101, 2);

        // Then
        assertEquals(1, result);
        verify(dormRoomMapper).update(any(), any());
    }

    @Test
    void testDeleteBedInfo_Failure() {
        // Given
        when(dormRoomMapper.update(any(), any())).thenReturn(0);

        // When
        int result = dormRoomService.deleteBedInfo("床1", 101, 2);

        // Then
        assertEquals(0, result);
        verify(dormRoomMapper).update(any(), any());
    }

    @Test
    void testJudgeHadBed_Success() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(testDormRoom);

        // When
        DormRoom result = dormRoomService.judgeHadBed("张三");

        // Then
        assertNotNull(result);
        assertEquals(101, result.getDormRoomId());
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testJudgeHadBed_NotFound() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(null);

        // When
        DormRoom result = dormRoomService.judgeHadBed("张三");

        // Then
        assertNull(result);
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testSelectHaveRoomStuNum_Success() {
        // Given
        when(dormRoomMapper.selectCount(any())).thenReturn(25L);

        // When
        Long result = dormRoomService.selectHaveRoomStuNum();

        // Then
        assertEquals(100L, result); // 4个床位各25人 = 100
        verify(dormRoomMapper, times(4)).selectCount(any());
    }

    @Test
    void testGetEachBuildingStuNum_Success() {
        // Given
        when(dormRoomMapper.selectCount(any())).thenReturn(10L);

        // When
        Long result = dormRoomService.getEachBuildingStuNum(1);

        // Then
        assertEquals(40L, result); // 4个床位各10人 = 40
        verify(dormRoomMapper, times(4)).selectCount(any());
    }

    @Test
    void testCheckRoomState_Success() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(testDormRoom);

        // When
        DormRoom result = dormRoomService.checkRoomState(101);

        // Then
        assertNotNull(result);
        assertEquals(101, result.getDormRoomId());
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testCheckRoomState_NotFound() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(null);

        // When
        DormRoom result = dormRoomService.checkRoomState(101);

        // Then
        assertNull(result);
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testCheckRoomExist_Success() {
        // Given
        when(dormRoomMapper.selectById(101)).thenReturn(testDormRoom);

        // When
        DormRoom result = dormRoomService.checkRoomExist(101);

        // Then
        assertNotNull(result);
        assertEquals(101, result.getDormRoomId());
        verify(dormRoomMapper).selectById(101);
    }

    @Test
    void testCheckRoomExist_NotFound() {
        // Given
        when(dormRoomMapper.selectById(101)).thenReturn(null);

        // When
        DormRoom result = dormRoomService.checkRoomExist(101);

        // Then
        assertNull(result);
        verify(dormRoomMapper).selectById(101);
    }

    @Test
    void testCheckBedState_Success() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(testDormRoom);

        // When
        DormRoom result = dormRoomService.checkBedState(101, 1);

        // Then
        assertNotNull(result);
        assertEquals(101, result.getDormRoomId());
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testCheckBedState_NotFound() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(null);

        // When
        DormRoom result = dormRoomService.checkBedState(101, 1);

        // Then
        assertNull(result);
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testReleaseBedByStudent() {
        // Given
        when(dormRoomMapper.findByStudentUsername("张三")).thenReturn(testDormRoom);
        when(dormRoomMapper.update(any(), any())).thenReturn(1);

        // When
        dormRoomService.releaseBedByStudent("张三");

        // Then
        verify(dormRoomMapper).findByStudentUsername("张三");
        verify(dormRoomMapper).update(any(), any());
    }

    @Test
    void testAddRoomEvaluation_Success() {
        // Given
        when(dormRoomMapper.update(any(), any())).thenReturn(1);

        // When
        int result = dormRoomService.addRoomEvaluation(101, "很好");

        // Then
        assertEquals(1, result);
        verify(dormRoomMapper).update(any(), any());
    }

    @Test
    void testAddRoomEvaluation_Failure() {
        // Given
        when(dormRoomMapper.update(any(), any())).thenReturn(0);

        // When
        int result = dormRoomService.addRoomEvaluation(101, "很好");

        // Then
        assertEquals(0, result);
        verify(dormRoomMapper).update(any(), any());
    }

    @Test
    void testGetRoomEvaluation_Success() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(testDormRoom);

        // When
        DormRoom result = dormRoomService.getRoomEvaluation(101);

        // Then
        assertNotNull(result);
        assertEquals(101, result.getDormRoomId());
        assertEquals("很好", result.getEvaluation());
        verify(dormRoomMapper).selectOne(any());
    }

    @Test
    void testGetRoomEvaluation_NotFound() {
        // Given
        when(dormRoomMapper.selectOne(any())).thenReturn(null);

        // When
        DormRoom result = dormRoomService.getRoomEvaluation(101);

        // Then
        assertNull(result);
        verify(dormRoomMapper).selectOne(any());
    }
} 