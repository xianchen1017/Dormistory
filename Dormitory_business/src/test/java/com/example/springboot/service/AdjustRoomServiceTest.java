package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.AdjustRoom;
import com.example.springboot.service.impl.AdjustRoomServiceImpl;
import com.example.springboot.mapper.AdjustRoomMapper;
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
class AdjustRoomServiceTest {

    @Mock
    private AdjustRoomMapper adjustRoomMapper;

    @InjectMocks
    private AdjustRoomServiceImpl adjustRoomService;

    private AdjustRoom testAdjustRoom;

    @BeforeEach
    void setUp() {
        testAdjustRoom = new AdjustRoom();
        testAdjustRoom.setId(1);
        testAdjustRoom.setUsername("2021001");
        testAdjustRoom.setName("张三");
        testAdjustRoom.setCurrentRoomId(101);
        testAdjustRoom.setCurrentBedId(1);
        testAdjustRoom.setTowardsRoomId(102);
        testAdjustRoom.setTowardsBedId(2);
        testAdjustRoom.setState("待审核");
        testAdjustRoom.setApplyTime("2024-01-15 10:00:00");
        testAdjustRoom.setFinishTime(null);
    }

    @Test
    void testAddApply_Success() {
        when(adjustRoomMapper.insert(any(AdjustRoom.class))).thenReturn(1);
        int result = adjustRoomService.addApply(testAdjustRoom);
        assertEquals(1, result);
        verify(adjustRoomMapper).insert(testAdjustRoom);
    }

    @Test
    void testAddApply_Failure() {
        when(adjustRoomMapper.insert(any(AdjustRoom.class))).thenReturn(0);
        int result = adjustRoomService.addApply(testAdjustRoom);
        assertEquals(0, result);
        verify(adjustRoomMapper).insert(testAdjustRoom);
    }

    @Test
    void testFind_Success() {
        Page<AdjustRoom> page = new Page<>(1, 10);
        List<AdjustRoom> list = Arrays.asList(testAdjustRoom);
        page.setRecords(list);
        page.setTotal(1);
        when(adjustRoomMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page result = adjustRoomService.find(1, 10, "2021001");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(adjustRoomMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateApply_Success() {
        when(adjustRoomMapper.updateById(any(AdjustRoom.class))).thenReturn(1);
        int result = adjustRoomService.updateApply(testAdjustRoom);
        assertEquals(1, result);
        verify(adjustRoomMapper).updateById(testAdjustRoom);
    }

    @Test
    void testUpdateApply_Failure() {
        when(adjustRoomMapper.updateById(any(AdjustRoom.class))).thenReturn(0);
        int result = adjustRoomService.updateApply(testAdjustRoom);
        assertEquals(0, result);
        verify(adjustRoomMapper).updateById(testAdjustRoom);
    }

    @Test
    void testDeleteAdjustment_Success() {
        when(adjustRoomMapper.deleteById(1)).thenReturn(1);
        int result = adjustRoomService.deleteAdjustment(1);
        assertEquals(1, result);
        verify(adjustRoomMapper).deleteById(1);
    }

    @Test
    void testDeleteAdjustment_Failure() {
        when(adjustRoomMapper.deleteById(1)).thenReturn(0);
        int result = adjustRoomService.deleteAdjustment(1);
        assertEquals(0, result);
        verify(adjustRoomMapper).deleteById(1);
    }
} 