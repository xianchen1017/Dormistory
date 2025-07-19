package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.Notice;
import com.example.springboot.service.impl.NoticeServiceImpl;
import com.example.springboot.mapper.NoticeMapper;
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
class NoticeServiceTest {

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    private Notice testNotice;

    @BeforeEach
    void setUp() {
        testNotice = new Notice();
        testNotice.setId(1);
        testNotice.setTitle("重要通知");
        testNotice.setContent("关于宿舍卫生检查的通知");
        testNotice.setReleaseTime("2024-01-15");
        testNotice.setAuthor("管理员");
    }

    @Test
    void testAddNewNotice_Success() {
        // Given
        when(noticeMapper.insert(any(Notice.class))).thenReturn(1);

        // When
        int result = noticeService.addNewNotice(testNotice);

        // Then
        assertEquals(1, result);
        verify(noticeMapper).insert(testNotice);
    }

    @Test
    void testAddNewNotice_Failure() {
        // Given
        when(noticeMapper.insert(any(Notice.class))).thenReturn(0);

        // When
        int result = noticeService.addNewNotice(testNotice);

        // Then
        assertEquals(0, result);
        verify(noticeMapper).insert(testNotice);
    }

    @Test
    void testFind_Success() {
        // Given
        Page<Notice> page = new Page<>(1, 10);
        List<Notice> notices = Arrays.asList(testNotice);
        page.setRecords(notices);
        page.setTotal(1);
        
        when(noticeMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Notice> result = noticeService.find(1, 10, "");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(noticeMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithSearch() {
        // Given
        Page<Notice> page = new Page<>(1, 10);
        List<Notice> notices = Arrays.asList(testNotice);
        page.setRecords(notices);
        page.setTotal(1);
        
        when(noticeMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Notice> result = noticeService.find(1, 10, "重要");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(noticeMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateNewNotice_Success() {
        // Given
        when(noticeMapper.updateById(any(Notice.class))).thenReturn(1);

        // When
        int result = noticeService.updateNewNotice(testNotice);

        // Then
        assertEquals(1, result);
        verify(noticeMapper).updateById(testNotice);
    }

    @Test
    void testUpdateNewNotice_Failure() {
        // Given
        when(noticeMapper.updateById(any(Notice.class))).thenReturn(0);

        // When
        int result = noticeService.updateNewNotice(testNotice);

        // Then
        assertEquals(0, result);
        verify(noticeMapper).updateById(testNotice);
    }

    @Test
    void testDeleteNotice_Success() {
        // Given
        when(noticeMapper.deleteById(1)).thenReturn(1);

        // When
        int result = noticeService.deleteNotice(1);

        // Then
        assertEquals(1, result);
        verify(noticeMapper).deleteById(1);
    }

    @Test
    void testDeleteNotice_Failure() {
        // Given
        when(noticeMapper.deleteById(1)).thenReturn(0);

        // When
        int result = noticeService.deleteNotice(1);

        // Then
        assertEquals(0, result);
        verify(noticeMapper).deleteById(1);
    }

    @Test
    void testHomePageNotice_Success() {
        // Given
        List<Notice> notices = Arrays.asList(testNotice);
        when(noticeMapper.selectList(any())).thenReturn(notices);

        // When
        List<?> result = noticeService.homePageNotice();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(noticeMapper).selectList(any());
    }

    @Test
    void testGetById_Success() {
        // Given
        when(noticeMapper.selectById(1)).thenReturn(testNotice);

        // When
        Notice result = noticeService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("重要通知", result.getTitle());
        verify(noticeMapper).selectById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(noticeMapper.selectById(1)).thenReturn(null);

        // When
        Notice result = noticeService.getById(1);

        // Then
        assertNull(result);
        verify(noticeMapper).selectById(1);
    }
} 