package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.DormManager;
import com.example.springboot.service.impl.DormManagerServiceImpl;
import com.example.springboot.mapper.DormManagerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DormManagerServiceTest {

    @Mock
    private DormManagerMapper dormManagerMapper;

    @Spy
    @InjectMocks
    private DormManagerServiceImpl dormManagerService;

    private DormManager testDormManager;

    @BeforeEach
    void setUp() {
        testDormManager = new DormManager();
        testDormManager.setUsername("manager001");
        testDormManager.setPassword("123456");
        testDormManager.setDormBuildId(101);
        testDormManager.setName("张管理员");
        testDormManager.setGender("男");
        testDormManager.setAge(35);
        testDormManager.setPhoneNum("13800138000");
        testDormManager.setEmail("manager@example.com");
        testDormManager.setAvatar("avatar.jpg");
        testDormManager.setClassroom("101办公室");
    }

    @Test
    void testDormManagerLogin_Success() {
        when(dormManagerMapper.selectOne(any())).thenReturn(testDormManager);
        DormManager result = dormManagerService.dormManagerLogin("manager001", "123456");
        assertNotNull(result);
        assertEquals("manager001", result.getUsername());
        assertEquals("123456", result.getPassword());
        verify(dormManagerMapper).selectOne(any());
    }

    @Test
    void testDormManagerLogin_Failure() {
        when(dormManagerMapper.selectOne(any())).thenReturn(null);
        DormManager result = dormManagerService.dormManagerLogin("manager001", "wrongpassword");
        assertNull(result);
        verify(dormManagerMapper).selectOne(any());
    }

    @Test
    void testAddNewDormManager_Success() {
        when(dormManagerMapper.insert(any(DormManager.class))).thenReturn(1);
        int result = dormManagerService.addNewDormManager(testDormManager);
        assertEquals(1, result);
        verify(dormManagerMapper).insert(testDormManager);
    }

    @Test
    void testAddNewDormManager_Failure() {
        when(dormManagerMapper.insert(any(DormManager.class))).thenReturn(0);
        int result = dormManagerService.addNewDormManager(testDormManager);
        assertEquals(0, result);
        verify(dormManagerMapper).insert(testDormManager);
    }

    @Test
    void testUpdateNewDormManager_Success() {
        when(dormManagerMapper.updateById(any(DormManager.class))).thenReturn(1);
        int result = dormManagerService.updateNewDormManager(testDormManager);
        assertEquals(1, result);
        verify(dormManagerMapper).updateById(testDormManager);
    }

    @Test
    void testUpdateNewDormManager_Failure() {
        when(dormManagerMapper.updateById(any(DormManager.class))).thenReturn(0);
        int result = dormManagerService.updateNewDormManager(testDormManager);
        assertEquals(0, result);
        verify(dormManagerMapper).updateById(testDormManager);
    }

    @Test
    void testDeleteDormManager_Success() {
        when(dormManagerMapper.deleteById("manager001")).thenReturn(1);
        int result = dormManagerService.deleteDormManager("manager001");
        assertEquals(1, result);
        verify(dormManagerMapper).deleteById("manager001");
    }

    @Test
    void testDeleteDormManager_Failure() {
        when(dormManagerMapper.deleteById("manager001")).thenReturn(0);
        int result = dormManagerService.deleteDormManager("manager001");
        assertEquals(0, result);
        verify(dormManagerMapper).deleteById("manager001");
    }

    @Test
    void testFind_Success() {
        Page<DormManager> page = new Page<>(1, 10);
        List<DormManager> list = Arrays.asList(testDormManager);
        page.setRecords(list);
        page.setTotal(1);
        when(dormManagerMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page result = dormManagerService.find(1, 10, "张管理员");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(dormManagerMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithEmptySearch() {
        Page<DormManager> page = new Page<>(1, 10);
        List<DormManager> list = Arrays.asList(testDormManager);
        page.setRecords(list);
        page.setTotal(1);
        when(dormManagerMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page result = dormManagerService.find(1, 10, "");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(dormManagerMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithNullSearch() {
        Page<DormManager> page = new Page<>(1, 10);
        List<DormManager> list = Arrays.asList(testDormManager);
        page.setRecords(list);
        page.setTotal(1);
        when(dormManagerMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page result = dormManagerService.find(1, 10, null);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(dormManagerMapper).selectPage(any(Page.class), any());
    }
} 