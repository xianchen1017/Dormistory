package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.Repair;
import com.example.springboot.service.impl.RepairServiceImpl;
import com.example.springboot.mapper.RepairMapper;
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
class RepairServiceTest {

    @Mock
    private RepairMapper repairMapper;

    @InjectMocks
    private RepairServiceImpl repairService;

    private Repair testRepair;

    @BeforeEach
    void setUp() {
        testRepair = new Repair();
        testRepair.setId(1);
        testRepair.setDormRoomId(101);
        testRepair.setTitle("水龙头漏水");
        testRepair.setContent("卫生间水龙头一直漏水，需要维修");
        testRepair.setOrderBuildTime("2024-01-15");
        testRepair.setState("待处理");
        testRepair.setRepairer("张三");
        testRepair.setDormBuildId(1);
    }

    @Test
    void testAddNewOrder_Success() {
        // Given
        when(repairMapper.insert(any(Repair.class))).thenReturn(1);

        // When
        int result = repairService.addNewOrder(testRepair);

        // Then
        assertEquals(1, result);
        verify(repairMapper).insert(testRepair);
    }

    @Test
    void testAddNewOrder_Failure() {
        // Given
        when(repairMapper.insert(any(Repair.class))).thenReturn(0);

        // When
        int result = repairService.addNewOrder(testRepair);

        // Then
        assertEquals(0, result);
        verify(repairMapper).insert(testRepair);
    }

    @Test
    void testFind_Success() {
        // Given
        Page<Repair> page = new Page<>(1, 10);
        List<Repair> repairs = Arrays.asList(testRepair);
        page.setRecords(repairs);
        page.setTotal(1);
        
        when(repairMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Repair> result = repairService.find(1, 10, "");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(repairMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithSearch() {
        // Given
        Page<Repair> page = new Page<>(1, 10);
        List<Repair> repairs = Arrays.asList(testRepair);
        page.setRecords(repairs);
        page.setTotal(1);
        
        when(repairMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Repair> result = repairService.find(1, 10, "水龙头");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(repairMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateNewOrder_Success() {
        // Given
        when(repairMapper.updateById(any(Repair.class))).thenReturn(1);

        // When
        int result = repairService.updateNewOrder(testRepair);

        // Then
        assertEquals(1, result);
        verify(repairMapper).updateById(testRepair);
    }

    @Test
    void testUpdateNewOrder_Failure() {
        // Given
        when(repairMapper.updateById(any(Repair.class))).thenReturn(0);

        // When
        int result = repairService.updateNewOrder(testRepair);

        // Then
        assertEquals(0, result);
        verify(repairMapper).updateById(testRepair);
    }

    @Test
    void testDeleteOrder_Success() {
        // Given
        when(repairMapper.deleteById(1)).thenReturn(1);

        // When
        int result = repairService.deleteOrder(1);

        // Then
        assertEquals(1, result);
        verify(repairMapper).deleteById(1);
    }

    @Test
    void testDeleteOrder_Failure() {
        // Given
        when(repairMapper.deleteById(1)).thenReturn(0);

        // When
        int result = repairService.deleteOrder(1);

        // Then
        assertEquals(0, result);
        verify(repairMapper).deleteById(1);
    }

    @Test
    void testShowOrderNum_Success() {
        // Given
        when(repairMapper.selectCount(any())).thenReturn(50L);

        // When
        int result = repairService.showOrderNum();

        // Then
        assertEquals(50, result);
        verify(repairMapper).selectCount(any());
    }

    @Test
    void testGetById_Success() {
        // Given
        when(repairMapper.selectById(1)).thenReturn(testRepair);

        // When
        Repair result = repairService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("水龙头漏水", result.getTitle());
        verify(repairMapper).selectById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(repairMapper.selectById(1)).thenReturn(null);

        // When
        Repair result = repairService.getById(1);

        // Then
        assertNull(result);
        verify(repairMapper).selectById(1);
    }
} 