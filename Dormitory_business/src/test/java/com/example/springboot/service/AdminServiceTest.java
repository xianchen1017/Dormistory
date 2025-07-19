package com.example.springboot.service;

import com.example.springboot.pojo.Admin;
import com.example.springboot.service.impl.AdminServiceImpl;
import com.example.springboot.mapper.AdminMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setUsername("admin001");
        testAdmin.setPassword("adminpass");
        testAdmin.setName("管理员");
        testAdmin.setGender("男");
        testAdmin.setAge(35);
        testAdmin.setPhoneNum("13800000000");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setAvatar("avatar.png");
        testAdmin.setClassroom("A101");
    }

    @Test
    void testAdminLogin_Success() {
        when(adminMapper.selectOne(any())).thenReturn(testAdmin);
        Admin result = adminService.adminLogin("admin001", "adminpass");
        assertNotNull(result);
        assertEquals("admin001", result.getUsername());
        assertEquals("adminpass", result.getPassword());
        verify(adminMapper).selectOne(any());
    }

    @Test
    void testAdminLogin_Failure() {
        when(adminMapper.selectOne(any())).thenReturn(null);
        Admin result = adminService.adminLogin("admin001", "wrongpass");
        assertNull(result);
        verify(adminMapper).selectOne(any());
    }

    @Test
    void testUpdateAdmin_Success() {
        when(adminMapper.updateById(any(Admin.class))).thenReturn(1);
        int result = adminService.updateAdmin(testAdmin);
        assertEquals(1, result);
        verify(adminMapper).updateById(testAdmin);
    }

    @Test
    void testUpdateAdmin_Failure() {
        when(adminMapper.updateById(any(Admin.class))).thenReturn(0);
        int result = adminService.updateAdmin(testAdmin);
        assertEquals(0, result);
        verify(adminMapper).updateById(testAdmin);
    }
} 