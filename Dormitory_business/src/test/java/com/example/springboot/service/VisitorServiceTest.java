package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.Visitor;
import com.example.springboot.service.impl.VisitorServiceImpl;
import com.example.springboot.mapper.VisitorMapper;
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
class VisitorServiceTest {

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    private Visitor testVisitor;

    @BeforeEach
    void setUp() {
        testVisitor = new Visitor();
        testVisitor.setId(1);
        testVisitor.setVisitorName("李四");
        testVisitor.setPhoneNum("13900139000");
        testVisitor.setVisitTime("2024-01-15 14:30:00");
        testVisitor.setContent("探望同学");
        testVisitor.setGender("男");
        testVisitor.setOriginCity("北京");
    }

    @Test
    void testAddNewVisitor_Success() {
        // Given
        when(visitorMapper.insert(any(Visitor.class))).thenReturn(1);

        // When
        int result = visitorService.addNewVisitor(testVisitor);

        // Then
        assertEquals(1, result);
        verify(visitorMapper).insert(testVisitor);
    }

    @Test
    void testAddNewVisitor_Failure() {
        // Given
        when(visitorMapper.insert(any(Visitor.class))).thenReturn(0);

        // When
        int result = visitorService.addNewVisitor(testVisitor);

        // Then
        assertEquals(0, result);
        verify(visitorMapper).insert(testVisitor);
    }

    @Test
    void testFind_Success() {
        // Given
        Page<Visitor> page = new Page<>(1, 10);
        List<Visitor> visitors = Arrays.asList(testVisitor);
        page.setRecords(visitors);
        page.setTotal(1);
        
        when(visitorMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Visitor> result = visitorService.find(1, 10, "");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(visitorMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithSearch() {
        // Given
        Page<Visitor> page = new Page<>(1, 10);
        List<Visitor> visitors = Arrays.asList(testVisitor);
        page.setRecords(visitors);
        page.setTotal(1);
        
        when(visitorMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Visitor> result = visitorService.find(1, 10, "李四");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(visitorMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateNewVisitor_Success() {
        // Given
        when(visitorMapper.updateById(any(Visitor.class))).thenReturn(1);

        // When
        int result = visitorService.updateNewVisitor(testVisitor);

        // Then
        assertEquals(1, result);
        verify(visitorMapper).updateById(testVisitor);
    }

    @Test
    void testUpdateNewVisitor_Failure() {
        // Given
        when(visitorMapper.updateById(any(Visitor.class))).thenReturn(0);

        // When
        int result = visitorService.updateNewVisitor(testVisitor);

        // Then
        assertEquals(0, result);
        verify(visitorMapper).updateById(testVisitor);
    }

    @Test
    void testDeleteVisitor_Success() {
        // Given
        when(visitorMapper.deleteById(1)).thenReturn(1);

        // When
        int result = visitorService.deleteVisitor(1);

        // Then
        assertEquals(1, result);
        verify(visitorMapper).deleteById(1);
    }

    @Test
    void testDeleteVisitor_Failure() {
        // Given
        when(visitorMapper.deleteById(1)).thenReturn(0);

        // When
        int result = visitorService.deleteVisitor(1);

        // Then
        assertEquals(0, result);
        verify(visitorMapper).deleteById(1);
    }

    @Test
    void testGetById_Success() {
        // Given
        when(visitorMapper.selectById(1)).thenReturn(testVisitor);

        // When
        Visitor result = visitorService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("李四", result.getVisitorName());
        verify(visitorMapper).selectById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(visitorMapper.selectById(1)).thenReturn(null);

        // When
        Visitor result = visitorService.getById(1);

        // Then
        assertNull(result);
        verify(visitorMapper).selectById(1);
    }
} 