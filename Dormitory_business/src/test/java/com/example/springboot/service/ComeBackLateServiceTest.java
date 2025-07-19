package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.ComeBackLate;
import com.example.springboot.service.impl.ComeBackLateServiceImpl;
import com.example.springboot.mapper.ComeBackLateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ComeBackLateServiceTest {

    @Mock
    private ComeBackLateMapper comeBackLateMapper;

    @Spy
    @InjectMocks
    private ComeBackLateServiceImpl comeBackLateService;

    private ComeBackLate testComeBackLate;

    @BeforeEach
    void setUp() {
        testComeBackLate = new ComeBackLate();
        testComeBackLate.setId(1L);
        testComeBackLate.setStudentName("张三");
        testComeBackLate.setDormRoom("101");
        testComeBackLate.setLateTime(new Date());
        testComeBackLate.setReason("学习");
        testComeBackLate.setRemark("无");
    }

    @Test
    void testAdd_Success() {
        when(comeBackLateMapper.insert(any(ComeBackLate.class))).thenReturn(1);
        int result = comeBackLateService.add(testComeBackLate);
        assertEquals(1, result);
        verify(comeBackLateMapper).insert(testComeBackLate);
    }

    @Test
    void testAdd_Failure() {
        when(comeBackLateMapper.insert(any(ComeBackLate.class))).thenReturn(0);
        int result = comeBackLateService.add(testComeBackLate);
        assertEquals(0, result);
        verify(comeBackLateMapper).insert(testComeBackLate);
    }

    @Test
    void testUpdate_Success() {
        when(comeBackLateMapper.updateById(any(ComeBackLate.class))).thenReturn(1);
        int result = comeBackLateService.update(testComeBackLate);
        assertEquals(1, result);
        verify(comeBackLateMapper).updateById(testComeBackLate);
    }

    @Test
    void testUpdate_Failure() {
        when(comeBackLateMapper.updateById(any(ComeBackLate.class))).thenReturn(0);
        int result = comeBackLateService.update(testComeBackLate);
        assertEquals(0, result);
        verify(comeBackLateMapper).updateById(testComeBackLate);
    }

    @Test
    void testDelete_Success() {
        // 使用doReturn来mock removeById方法
        doReturn(true).when(comeBackLateService).removeById(1);
        int result = comeBackLateService.delete(1);
        assertEquals(1, result);
        verify(comeBackLateService).removeById(1);
    }

    @Test
    void testDelete_Failure() {
        // 使用doReturn来mock removeById方法
        doReturn(false).when(comeBackLateService).removeById(1);
        int result = comeBackLateService.delete(1);
        assertEquals(0, result);
        verify(comeBackLateService).removeById(1);
    }

    @Test
    void testFind_Success() {
        Page<ComeBackLate> page = new Page<>(1, 10);
        List<ComeBackLate> list = Arrays.asList(testComeBackLate);
        page.setRecords(list);
        page.setTotal(1);
        when(comeBackLateMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page<ComeBackLate> result = comeBackLateService.find(1, 10, "张三");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(comeBackLateMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFindByStudent_Success() {
        Page<ComeBackLate> page = new Page<>(1, 10);
        List<ComeBackLate> list = Arrays.asList(testComeBackLate);
        page.setRecords(list);
        page.setTotal(1);
        when(comeBackLateMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page<ComeBackLate> result = comeBackLateService.findByStudent(1, 10, "2021001");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(comeBackLateMapper).selectPage(any(Page.class), any());
    }
} 