package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.Student;
import com.example.springboot.service.impl.StudentServiceImpl;
import com.example.springboot.mapper.StudentMapper;
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
class StudentServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setUsername("2021001");
        testStudent.setPassword("123456");
        testStudent.setName("张三");
        testStudent.setGender("男");
        testStudent.setPhoneNum("13800138000");
        testStudent.setAge(20);
        testStudent.setEmail("zhangsan@example.com");
    }

    @Test
    void testStuLogin_Success() {
        // Given
        when(studentMapper.selectOne(any())).thenReturn(testStudent);

        // When
        Student result = studentService.stuLogin("2021001", "123456");

        // Then
        assertNotNull(result);
        assertEquals("2021001", result.getUsername());
        assertEquals("123456", result.getPassword());
        verify(studentMapper).selectOne(any());
    }

    @Test
    void testStuLogin_WrongPassword() {
        // Given
        when(studentMapper.selectOne(any())).thenReturn(testStudent);

        // When
        Student result = studentService.stuLogin("2021001", "wrongpassword");

        // Then
        assertNull(result);
        verify(studentMapper).selectOne(any());
    }

    @Test
    void testStuLogin_UserNotFound() {
        // Given
        when(studentMapper.selectOne(any())).thenReturn(null);

        // When
        Student result = studentService.stuLogin("2021001", "123456");

        // Then
        assertNull(result);
        verify(studentMapper).selectOne(any());
    }

    @Test
    void testAddNewStudent_Success() {
        // Given
        when(studentMapper.insert(any(Student.class))).thenReturn(1);

        // When
        int result = studentService.addNewStudent(testStudent);

        // Then
        assertEquals(1, result);
        verify(studentMapper).insert(testStudent);
    }

    @Test
    void testAddNewStudent_Failure() {
        // Given
        when(studentMapper.insert(any(Student.class))).thenReturn(0);

        // When
        int result = studentService.addNewStudent(testStudent);

        // Then
        assertEquals(0, result);
        verify(studentMapper).insert(testStudent);
    }

    @Test
    void testAddNewStudent_Exception() {
        // Given
        when(studentMapper.insert(any(Student.class))).thenThrow(new RuntimeException("数据库连接失败"));

        // When
        int result = studentService.addNewStudent(testStudent);

        // Then
        assertEquals(0, result);
        verify(studentMapper).insert(testStudent);
    }

    @Test
    void testFind_Success() {
        // Given
        Page<Student> page = new Page<>(1, 10);
        List<Student> students = Arrays.asList(testStudent);
        page.setRecords(students);
        page.setTotal(1);
        
        when(studentMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Student> result = studentService.find(1, 10, "");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(studentMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testFind_WithSearch() {
        // Given
        Page<Student> page = new Page<>(1, 10);
        List<Student> students = Arrays.asList(testStudent);
        page.setRecords(students);
        page.setTotal(1);
        
        when(studentMapper.selectPage(any(Page.class), any())).thenReturn(page);

        // When
        Page<Student> result = studentService.find(1, 10, "张三");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        verify(studentMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testUpdateNewStudent_Success() {
        // Given
        when(studentMapper.updateById(any(Student.class))).thenReturn(1);

        // When
        int result = studentService.updateNewStudent(testStudent);

        // Then
        assertEquals(1, result);
        verify(studentMapper).updateById(testStudent);
    }

    @Test
    void testUpdateNewStudent_Failure() {
        // Given
        when(studentMapper.updateById(any(Student.class))).thenReturn(0);

        // When
        int result = studentService.updateNewStudent(testStudent);

        // Then
        assertEquals(0, result);
        verify(studentMapper).updateById(testStudent);
    }

    @Test
    void testDeleteStudent_Success() {
        // Given
        when(studentMapper.deleteById("2021001")).thenReturn(1);

        // When
        int result = studentService.deleteStudent("2021001");

        // Then
        assertEquals(1, result);
        verify(studentMapper).deleteById("2021001");
    }

    @Test
    void testDeleteStudent_Failure() {
        // Given
        when(studentMapper.deleteById("2021001")).thenReturn(0);

        // When
        int result = studentService.deleteStudent("2021001");

        // Then
        assertEquals(0, result);
        verify(studentMapper).deleteById("2021001");
    }

    @Test
    void testStuNum_Success() {
        // Given
        when(studentMapper.selectCount(any())).thenReturn(100L);

        // When
        int result = studentService.stuNum();

        // Then
        assertEquals(100, result);
        verify(studentMapper).selectCount(any());
    }

    @Test
    void testStuInfo_Success() {
        // Given
        when(studentMapper.selectOne(any())).thenReturn(testStudent);

        // When
        Student result = studentService.stuInfo("2021001");

        // Then
        assertNotNull(result);
        assertEquals("2021001", result.getUsername());
        assertEquals("张三", result.getName());
        verify(studentMapper).selectOne(any());
    }

    @Test
    void testStuInfo_NotFound() {
        // Given
        when(studentMapper.selectOne(any())).thenReturn(null);

        // When
        Student result = studentService.stuInfo("2021001");

        // Then
        assertNull(result);
        verify(studentMapper).selectOne(any());
    }
} 